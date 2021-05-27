package truckapp.controllers;

import truckapp.model.Employee;
import truckapp.model.Truck;
import truckapp.model.User;
import truckapp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер админа
 *
 * @author kanenkovaa
 * @version 0.1
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Возврат страницы администратора
     *
     * @param model модель веб-страницы
     * @return admin - страница администратора
     */
    @GetMapping("/main")
    public String adminPage(@AuthenticationPrincipal User user, Model model) {
        return adminService.getAdminPage(user, model);
    }

    /**
     * метод поиска заказов указанного пользователя
     * @param user авторизированный пользователь
     * @param username имя пользователя, заказы которого нужно найти
     * @param model модель веб-страницы
     * @return перенаправление на страницу администратора или возврат страницы администратора
     */
    @GetMapping("/searchByUsername")
    public String searchForUsername(@AuthenticationPrincipal User user, @RequestParam("username") String username, Model model) {
        adminService.checkUserActivationCode(user, model);
        return adminService.searchUsers(username, model);
    }

    /**
     * метод поиска сотрудников
     * @param user авторизированный пользователь
     * @param employee имя сотрудника
     * @param model модель веб-страницы
     * @return перенаправление на страницу администратора или возврат страницы администратора
     */
    @GetMapping("/searchEmployee")
    public String searchEmployee(@AuthenticationPrincipal User user, @RequestParam("employee") String employee, Model model) {
        adminService.checkUserActivationCode(user, model);
        return adminService.checkEmployees(employee, model);
    }

    @GetMapping("addNewEmployeeOrCar")
    public String getPageAddNewEmployeeOrCar(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("car-form", new Truck());
        model.addAttribute("employee-form", new Employee());
        return "newEmployeeOrCar";
    }

    @PostMapping("/addCar")
    public String addNewCar(@ModelAttribute("car-form") Truck truck,
                            @RequestParam("truckDescription") String truckDescription,
                            @AuthenticationPrincipal User user,
                            BindingResult bindingResult,
                            Model model) {
        truck.setDescription(truckDescription);
        return adminService.addingCar(truck, user, bindingResult, model);
    }

    @PostMapping("/addEmployee")
    public String addNewEmployee(@ModelAttribute("employee-form") Employee employee,
                                 @AuthenticationPrincipal User user,
                                 BindingResult bindingResult,
                                 Model model) {
        return adminService.addingEmployee(employee, user, bindingResult, model);
    }
}
