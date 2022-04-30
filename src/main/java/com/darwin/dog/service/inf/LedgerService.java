package com.darwin.dog.service.inf;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.dto.in.CreateLedgerInDTO;
import com.darwin.dog.dto.in.UpdateLedgerDTO;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.po.User;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public interface LedgerService extends BasicService {

    BigDecimal sumExpense(Long ledgerID);

    BigDecimal sumIncome(Long ledgerID);

    /**
     * 通过获取账本，可能会发生未命中异常
     * @param ID 账本 ID
     * @return 账本
     */
    Ledger getLedgerByID(long ID);

    /**
     * 新建账本
     * @param createLedgerInDTO 新建账本数据传输对象
     * @return true 表示成功，false 表示失败
     */
    boolean create(CreateLedgerInDTO createLedgerInDTO);

    /**
     * 账本进行归档
     * @param ID 账本 ID
     * @return true 表示归档成功，false 表示归档失败
     */
    boolean archive(long ID);

    /**
     * 删除账本
     * @param ID 欲删除的账本 ID
     * @return true 表示成功，false 表示失败
     */
    boolean delete(long ID);

    /**
     * 统计账单的数量
     * @param ledgerID 目标账本 ID
     * @return 实际的账单数量（未包括已删除的账单）
     */
    long countBills(long ledgerID);

    /**
     * 获取正在使用的账本
     * @return Ledger
     */
    Ledger getUsingLedger();

    /**
     * 获取全部账本包括已逻辑删除的数据
     * @return 全部账本
     */
    List<Ledger> getAll();

    /**
     * 获取全部的未删除标记账本
     * @return 未删除账本列表
     */
    List<Ledger> getNotDeleted();

    /**
     * 计算账本的结余
     * @param ledgerID 账本 ID
     * @return BigDecimal 类型的结余结果
     */
    BigDecimal surplus(long ledgerID);

    /**
     * 计算当前用户全部的未删除账本的结余之和
     * @return 总结余
     */
    BigDecimal sumSurplusForAllLedger();

    boolean setUsingLedger(Long ledgerID);

    boolean update(UpdateLedgerDTO updateLedgerDTO);

}
