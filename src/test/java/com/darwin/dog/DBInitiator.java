package com.darwin.dog;

import com.darwin.dog.constant.Roles;
import com.darwin.dog.dto.in.RegisteringUserInDTO;
import com.darwin.dog.po.sys.Permission;
import com.darwin.dog.po.sys.PermissionExpression;
import com.darwin.dog.po.sys.Role;
import com.darwin.dog.service.inf.*;
import com.darwin.dog.service.inf.sys.PermissionService;
import com.darwin.dog.service.inf.sys.RoleService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ResourceUtils;

import javax.activation.MimeTypeParseException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Rollback(value = false)
@Transactional
@SpringBootTest
public class DBInitiator {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private LedgerCoverService ledgerCoverService;

    @Autowired
    private SignoryService signoryService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Test
    public void initDB() throws MimeTypeParseException, IOException {
        initPermission();
        initRole();
        initUser();
        initCoins();
        initLedgerCovers();
    }

    @Test
    public void initPermission(){
        permissionService.create(Permission.of(PermissionExpression.parse("Apple:delete:*")));
        permissionService.create(Permission.of(PermissionExpression.parse("Apple:create:*")));
    }

    @Test
    public void initRole(){
        Role admin = Role.of(Roles.ADMIN.getRoleName());
        admin.setPermissions(permissionService.readAll());
        Role user = Role.of(Roles.USER.getRoleName());
        user.setPermissions(permissionService.readAll());
        roleService.create(admin);
        roleService.create(user);
    }


    @Test
    public void initUser() throws IOException, MimeTypeParseException {
        File avatarFile = ResourceUtils.getFile("classpath:avatar.jpg");
        MockMultipartFile avatar = new MockMultipartFile("file","av.jpg", MimeTypeUtils.IMAGE_JPEG_VALUE, FileUtils.openInputStream(avatarFile));
        RegisteringUserInDTO registeringUserInDTO = RegisteringUserInDTO.of("MM", RandomStringUtils.randomNumeric(10), "123456", avatar);
        userService.register(registeringUserInDTO);
    }

    @Test
    public void initCoins() throws IOException {
        File coinsFile = ResourceUtils.getFile("classpath:readOnly/coins.json");
        coinService.importJson(FileUtils.readFileToString(coinsFile, Charset.defaultCharset()));
    }

    @Test
    public void initLedgerCovers() throws IOException {
        File ledgerCoversFiles = ResourceUtils.getFile("classpath:readOnly/ledgerCovers.json");
        ledgerCoverService.importJson(FileUtils.readFileToString(ledgerCoversFiles, Charset.defaultCharset()));
    }

    @Test
    public void initSignorys() throws IOException {
        File incomeSignoriesFile = ResourceUtils.getFile("classpath:readOnly/incomeSignories.json");
        signoryService.importJsonForIncome(FileUtils.readFileToString(incomeSignoriesFile,Charset.defaultCharset()));

        File expenseSignoriesFile = ResourceUtils.getFile("classpath:readOnly/expenseSignories.json");
        signoryService.importJsonForExpense(FileUtils.readFileToString(expenseSignoriesFile,Charset.defaultCharset()));
    }

    @Test
    public void initAccountType() throws IOException {
        File accountTypesFile = ResourceUtils.getFile("classpath:readOnly/accountTypes.json");
        accountTypeService.importJson(FileUtils.readFileToString(accountTypesFile,Charset.defaultCharset()));
    }
}
