package kr.co.kmarket.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/config")
@Controller
public class ConfigurationController {

    @GetMapping("/basic")
    public String basic() {return "admin/configuration/admin_basicSetting";}

    @GetMapping("/banner")
    public String banner() {return "admin/configuration/admin_banner";}

    @GetMapping("/policy")
    public String policy() {return "admin/configuration/admin_policy";}

    @GetMapping("/category")
    public String category() {return "admin/configuration/admin_category";}

    @GetMapping("/version")
    public String version() {return "admin/configuration/admin_version";}
}
