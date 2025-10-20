package kr.co.kmarket.service;

import jakarta.transaction.Transactional;
import kr.co.kmarket.dto.*;
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

    public OrderDTO getOrderDetailByCustomer(int custNumber, String orderNumber) {
        return myMapper.selectOrderDetailByCustomer(custNumber, orderNumber);
    }

    public MemberDTO getSellerByManufacture(String manufacture) {
        return myMapper.selectSellerByManufacture(manufacture);
    }

    public void registerQna(QnaDTO dto) {
        myMapper.insertQna(dto);
    }

    public int updateOrderConfirmation(String orderNumber, int custNumber) {

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

    public int updateOrderCancel(String orderNumber, int custNumber) {
        return myMapper.updateOrderCancel(orderNumber, custNumber);
    }

    @Transactional
    public void registerReview(ProductReviewDTO reviewDTO, List<MultipartFile> images) {

        Integer stat = myMapper.selectOrderStat(
                reviewDTO.getOrder_number(),
                reviewDTO.getProd_number(),
                reviewDTO.getCust_number()
        );

        // STAT이 8이 아니거나, 주문 상품이 조회되지 않은 경우
        if (stat == null || stat != PURCHASE_CONFIRMED_CODE) { // 💡 상수 사용
            log.warn("리뷰 작성 실패: 주문 {} 상품 {} 상태가 구매 확정({})이 아님. 현재 상태: {}",
                    reviewDTO.getOrder_number(), reviewDTO.getProd_number(), PURCHASE_CONFIRMED_CODE, stat);
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

    public int orderReturn(OrderDTO orderDTO) {
        OrderDTO stat = myMapper.selectOrderStat1(
                orderDTO.getOrder_number(),
                orderDTO.getCust_number()
        );

        if (stat == null || stat.getStat() != DELIVERY_COMPLETE_CODE) {
            return 0;
        }

        return myMapper.orderReturn(orderDTO);
    }

    public int orderExchange(OrderDTO orderDTO) {
        OrderDTO stat = myMapper.selectOrderStat1(
                orderDTO.getOrder_number(),
                orderDTO.getCust_number()
        );

        if (stat == null || stat.getStat() != DELIVERY_COMPLETE_CODE) {
            return 0;
        }

        return myMapper.orderExchange(orderDTO);
    }

    public int updateEmail(int custNumber, String email) {
        return myMapper.updateEmail(custNumber, email);
    }

    public int updateHp(int custNumber, String hp) {
        return myMapper.updateHp(custNumber, hp);
    }

    public int updateAddr(MemberDTO memberDTO) {
        return myMapper.updateAddr(memberDTO);
    }

    public MemberDTO selectUserOptions(int custNumber) {
        return myMapper.selectUserOptions(custNumber);
    }

    public MemberDTO selectUserInfo(int custNumber) {
        return myMapper.selectUserInfo(custNumber);
    }

    public int selectQnaCountByUserId(String user_id) {
        return myMapper.selectQnaCountByUserId(user_id);
    }

    public List<QnaDTO> selectQnaListPage(String user_id, int start, int limit) {
        // start는 offset, limit은 size 역할을 합니다.
        return myMapper.selectQnaListPage(user_id, start, limit);
    }

    public List<ProductReviewDTO> selectReviewsListPage(int custNumber, int start, int limit) {
        return myMapper.selectReviewsListPage(custNumber, start, limit);
    }

    public int selectReviewCountByCustNumber(int custNumber) {
        return myMapper.selectReviewCountByCustNumber(custNumber);
    }

    public int selectOrderCountByCustNumber(int custNumber) {
        return myMapper.selectOrderCountByCustNumber(custNumber);
    }

    public List<OrderDTO> selectOrdersListPage(int custNumber, int start, int limit) {
        return myMapper.selectOrdersListPage(custNumber, start, limit);
    }

    public int selectWaitingQna(int custNumber) {
        return myMapper.selectWaitingQna(custNumber);
    }

    public List<CouponDTO> selectCouponsListPage(int cust_number, int start, int limit) {
        return myMapper.selectCouponsListPage(cust_number, start, limit);
    }

    public int selectCouponCountByCustNumber(int cust_number) {
        return myMapper.selectCouponCountByCustNumber(cust_number);
    }

    public int selectPointCountByCustNumber(int custNumber) {
        return myMapper.selectPointCountByCustNumber(custNumber);
    }

    public List<PointDTO> selectPointsListPage(int custNumber, int start, int limit) {
        return myMapper.selectPointsListPage(custNumber, start, limit);
    }

    public List<PointDTO> selectPoints(int custNumber){
        return myMapper.selectPoints(custNumber);
    }

    public int selectAllPoints(int custNumber) {
        return myMapper.selectAllPoints(custNumber);
    }

    public int selectCountCoupon() {
        return myMapper.selectCountCoupon();
    }
}