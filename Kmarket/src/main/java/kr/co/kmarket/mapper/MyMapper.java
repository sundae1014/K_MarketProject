package kr.co.kmarket.mapper;

import kr.co.kmarket.dto.MemberDTO;
import kr.co.kmarket.dto.OrderDTO;
import kr.co.kmarket.dto.ProductReviewDTO;
import kr.co.kmarket.dto.QnaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyMapper {

    public List<OrderDTO> selectRecentOrders(@Param("custNumber") int custNumber);

    public int countNotConfirmedOrders(@Param("custNumber") int custNumber);

    public OrderDTO selectOrderDetailByCustomer(@Param("custNumber") int custNumber,
                                                @Param("orderNumber") String orderNumber);

    public MemberDTO selectSellerByManufacture(@Param("manufacture") String manufacture);

    public int insertQna(QnaDTO qnaDTO);

    public int selectMaxQnaId();

    // 주문 상태(stat)를 조회
    public OrderDTO selectOrderStat1(@Param("orderNumber") String orderNumber,
                                    @Param("custNumber") int custNumber);

    // 구매 확정 상태를 업데이트
    public int updateOrderConfirmation(@Param("orderNumber") String orderNumber,
                                       @Param("custNumber") int custNumber);

    public int updateOrderCancel(@Param("orderNumber") String orderNumber,
                                 @Param("custNumber") int custNumber);

    public int insertReview(ProductReviewDTO reviewDTO);

    public Integer selectOrderStat(@Param("orderNumber") String orderNumber,
                                   @Param("prodNo") int prodNo,
                                   @Param("custNumber") int custNumber);

    public List<ProductReviewDTO> selectRecentReviews(@Param("custNumber") int custNumber);

    public List<QnaDTO> selectRecentQnas(@Param("user_id") String user_id);

    public int orderReturn(OrderDTO orderDTO);

    public int orderExchange(OrderDTO orderDTO);

    public int updateEmail(@Param("custNumber") int custNumber,
                           @Param("email") String email);

    public int updateHp(@Param("custNumber") int custNumber,
                        @Param("hp") String hp);

    public int updateAddr(MemberDTO memberDTO);

    public MemberDTO selectUserOptions(@Param("custNumber") int custNumber);

    public MemberDTO selectUserInfo(@Param("custNumber") int custNumber);

    public int selectQnaCountByUserId(String user_Id);

    public List<QnaDTO> selectQnaListPage(@Param("user_id") String user_id,
                                          @Param("start") int start,
                                          @Param("limit") int limit);
}
