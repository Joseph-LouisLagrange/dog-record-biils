package com.darwin.dog.service.inf;

import com.darwin.dog.constant.BillDeleteType;
import com.darwin.dog.dto.in.*;
import com.darwin.dog.dto.out.*;
import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Coin;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public interface BillService extends BasicService {

    boolean removeCompletely(Set<Long> IDs);

    long queryDeletedBillTypeCount();

    boolean recover(Long ID);

    boolean recover(Set<Long> IDs);

    boolean reduce(List<Bill> bills, int billDeleteType);

    List<BillsBlockDTO> readDeletedBillTypeBills();

    List<BillsBlockDTO> readDeletedBills();

    long queryDeletedCount();

    long queryBillsCount();

    AssetsRangeDetailDTO readBillsInAccount(QueryRangeAtAccountInDTO queryRangeAtAccountInDTO);

    List<Bill> countBillRanking(QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO);

    List<CategoryRankingItemDTO> countCategoryRanking(QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO);

    List<MoneySignoryPartOutDTO> countMoneySignoryPart(QueryRangesWithBillTypeInDTO queryRangesWithBillTypeInDTO);

    MoneyTrendOutDTO countMoneyTrend(QueryRangesWithBillTypeInDTO queryRangesInDTO);

    AmountDTO countMyAmount(QueryRangesInDTO queryRangesInDTO);
    /**
     * 以时间范围来查询 Bills,并保证结果的时间有序性
     *
     * @param start 开始时间 (包括)
     * @param end   结束时间 (包括)
     * @return 时间降序的 Bill 列表
     */
    List<Bill> getMyBillsByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * 创建新的订单
     * @param createBillInDTO 新订单 DTO
     * @return true 新建成功
     */
    boolean createBill(CreateBillInDTO createBillInDTO);

    boolean delete(Long ID);

    Bill readByID(Long ID);

    boolean deleteInLedger(Long ledgerID);

    boolean deleteInAccount(Long accountID);

    Bill update(UpdateBillDTO updateBillDTO);

    LedgerRangeDetailDTO readLedgerForDateRanges(QueryRangeAtLedgerInDTO queryRangeAtLedgerInDTO);

    /**
     *
     * @param ledgerID
     * @param start
     * @param end
     * @return
     */
    LedgerRangeDetailDTO getMyBillsForDateRangeAndLedger(Long ledgerID, LocalDateTime start, LocalDateTime end);

    /**
     * 获取今日账单
     *
     * @return 今日时间降序的 Bill 列表
     */
    @PreAuthorize("isAuthenticated()")
    default List<Bill> getTodayMyBills() {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = start.plusDays(1).minusSeconds(1);
        return getMyBillsByDateRange(start, end);
    }

    List<Bill> searchForKeyword(@NotBlank String keyword);

    BigDecimal sum(List<Bill> bills, Coin baseCoin);

    BigDecimal sum(List<Bill> bills, String baseCoin);

}
