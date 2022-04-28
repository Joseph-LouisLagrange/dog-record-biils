package com.darwin.dog.service.inf;

import com.darwin.dog.dto.in.CreateBillInDTO;
import com.darwin.dog.dto.in.QueryRangeInDTO;
import com.darwin.dog.dto.out.LedgerRangeDetailDTO;
import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Coin;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public interface BillService extends BasicService {
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

    boolean deleteInLedger(Long ledgerID);

    LedgerRangeDetailDTO readLedgerForDateRanges(QueryRangeInDTO queryRangeInDTO);

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
