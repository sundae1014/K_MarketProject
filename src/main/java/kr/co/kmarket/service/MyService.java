package kr.co.kmarket.service;

import jakarta.transaction.Transactional;
import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.QnaDTO;
import kr.co.kmarket.mapper.MyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyService {

    private final MyMapper myMapper;

    @Value("${file.upload.path}")
    private String fileUploadPath;

    private static final int DELIVERY_COMPLETE_CODE = 4;
    private static final int PURCHASE_CONFIRMED_CODE = 8;
    private static final int CANCELED_CODE = 9;


    public List<OrderDTO> getRecentOrders(int custNumber) {
        return myMapper.selectRecentOrders(custNumber);
    }

    public int getNotConfirmedOrderCount(int custNumber) {
        return myMapper.countNotConfirmedOrders(custNumber);
    }

    public OrderDTO getOrderDetailByCustomer(int custNumber, int orderNumber) {
        return myMapper.selectOrderDetailByCustomer(custNumber, orderNumber);
    }

    public MemberDTO getSellerByManufacture(String manufacture) {
        return myMapper.selectSellerByManufacture(manufacture);
    }

    public void registerQna(QnaDTO dto) {
        myMapper.insertQna(dto);
    }

    public int updateOrderConfirmation(int orderNumber, int custNumber) {

        // 1. 현재 주문 정보(stat 포함)를 DB에서 조회
        OrderDTO order = myMapper.selectOrderStat1(orderNumber, custNumber);

        if (order == null || order.getStat() != DELIVERY_COMPLETE_CODE) {
            log.warn("구매 확정 실패: 주문 번호 {}는 현재 상태({})로 구매 확정이 불가합니다.",
                    orderNumber, order == null ? "NULL" : order.getStat());
            return 0; // 상태 불일치
        }

        // 3. 검증 통과 시 구매 확정 로직 실행 (Mapper에서 STAT을 8로 업데이트)
        return myMapper.updateOrderConfirmation(orderNumber, custNumber);
    }

    public int updateOrderCancel(int orderNumber, int custNumber) {
        return myMapper.updateOrderCancel(orderNumber, custNumber);
    }

    @Transactional
    public void registerReview(ProductReviewDTO reviewDTO, List<MultipartFile> images) {

        Integer stat = myMapper.selectOrderStat(
                reviewDTO.getOrderNumber(),
                reviewDTO.getProd_number(),
                reviewDTO.getCust_number()
        );

        // STAT이 8이 아니거나, 주문 상품이 조회되지 않은 경우
        if (stat == null || stat != PURCHASE_CONFIRMED_CODE) { // 💡 상수 사용
            log.warn("리뷰 작성 실패: 주문 {} 상품 {} 상태가 구매 확정({})이 아님. 현재 상태: {}",
                    reviewDTO.getOrderNumber(), reviewDTO.getProd_number(), PURCHASE_CONFIRMED_CODE, stat);
            // IllegalStateException을 던져 Controller로 오류 전달
            throw new IllegalStateException("구매 확정된 상품에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        // 2. 파일 처리 및 저장
        if (images != null && !images.isEmpty()) {
            processAndSetFileNames(reviewDTO, images);
        }

        // 3. 리뷰 정보 DB에 삽입 (검증 통과 후 실행)
        myMapper.insertReview(reviewDTO);
    }

    /**
     * 첨부 파일 저장 및 DTO에 파일명 설정 (최대 3개)
     */
    private void processAndSetFileNames(ProductReviewDTO reviewDTO, List<MultipartFile> images) {

        String path = fileUploadPath + "/review/";
        File uploadDir = new File(path);

        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                log.error("파일 업로드 디렉토리 생성 실패: {}", path);
                throw new RuntimeException("파일 업로드 디렉토리 생성에 실패했습니다.");
            }
        }

        for (int i = 0; i < Math.min(images.size(), 3); i++) {
            MultipartFile file = images.get(i);

            if (!file.isEmpty()) {
                String oriName = file.getOriginalFilename();
                String ext = oriName.substring(oriName.lastIndexOf("."));
                String newName = UUID.randomUUID().toString() + ext;

                try {
                    File saveFile = new File(path, newName); // 💡 저장 파일 객체 생성
                    file.transferTo(saveFile);

                    if (i == 0) {
                        reviewDTO.setR_img1(newName);
                    } else if (i == 1) {
                        reviewDTO.setR_img2(newName);
                    } else if (i == 2) {
                        reviewDTO.setR_img3(newName);
                    }

                } catch (IOException e) {
                    log.error("리뷰 파일 저장 오류", e);
                    throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
                }
            }
        }
    }

    public List<QnaDTO> getRecentQnas(String user_id) {
        return myMapper.selectRecentQnas(user_id);
    }

    public List<ProductReviewDTO> getRecentReviews(int custNumber) {
        return myMapper.selectRecentReviews(custNumber);
    }
}