package truckapp.controllers;

import truckapp.model.User;
import truckapp.roles.Role;
import truckapp.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер, отвечающий за регистрацию и атворизацию
 *
 * @author kanenkovaa
 * @version 0.1
 */
@Controller
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * Возврат страницы авторизации
     *
     * @param user объект авторизированного пользователя
     * @return страницу авторизации
     */
    @GetMapping("/login")
    public String authorizationPage(@RequestParam(required = false) String error, @AuthenticationPrincipal User user, Model model, HttpServletRequest request) {
        return authorizationService.checkAuthority(error, user, model, request);
    }

    /**
     *Возврат страницы регистрации
     *
     * @param user объект авторизированного пользовател
     * @param model a {@link org.springframework.ui.Model} object.
     * @return страницу регистрации
     */
    @GetMapping("/registration")
    public String registration(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("userForm", new User());
        return "registration";
    }

    /**
     * Непосредственная регистрация нового пользователя с проверкой вводимых данных
     *
     * @param userForm a {@link truckapp.model.User} object - объект пользователя из формы.
     * @param bindingResult a {@link org.springframework.validation.BindingResult} object - лист ошибок.
     * @param model a {@link org.springframework.ui.Model} object.
     * @return переадресация на домашнюю страницу
     */
    @PostMapping("/registrationAction")
    public String registrationAction(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        return authorizationService.save(userForm, bindingResult, model);
    }

    /**
     * метод активации учётной записи
     * @param code код активации учётной записи
     * @param model модель веб-страницы
     * @return страница авторизации
     */
    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        boolean isActivated = authorizationService.activateUser(code);
        if (isActivated)
            model.addAttribute("activate_success", "Учётная запись успешно активирована");
        else
            model.addAttribute("activate_fail", "Активация учётной записи не удалась");
        return "login";
    }

    /**
     * Метод получения страницы для восстановления пароля
     * @param user объект авторизированного пользователя
     * @param model модель веб-страницы
     * @return страница восстановления пароля
     */
    @GetMapping("/forgetPassword")
    public String getForgetPasswordPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "forgetPassword";
    }

    /**
     * Метод отпраки пользователю письма с ссылкой на страницу восстановления пароля
     * @param user объект авторизированного пользователя
     * @param email адрес эл.почты
     * @param model модель веб-страницы
     * @return страницу восстановления пароля или перенаправление на страницу авторизации
     */
    @PostMapping("/forgetPasswordAction")
    public String forgetPasswordAction(@AuthenticationPrincipal User user,
                                       @RequestParam("email") String email,
                                       Model model) {
        return authorizationService.forgetPasswordAction(user, email, model);
    }

    /**
     * Метод получения страницы восстановления пароля по ссылке из письма
     * @param user объект авторизированного пользователя
     * @param resetPasswordToken токен для восстановления пароля
     * @param model модель веб-страницы
     * @return страницу восстановления пароля
     */
    @GetMapping("/resetPassword/{resetPasswordToken}")
    public String getResetPasswordPage(@AuthenticationPrincipal User user,
                                       @PathVariable String resetPasswordToken,
                                       Model model) {
        model.addAttribute("user", user);
        model.addAttribute("token", resetPasswordToken);
        return "resetPassword";
    }

    /**
     * Метод установки нового пароля после утери старого
     * @param user объект авторизированного пользователя
     * @param token токен для восстановления пароля
     * @param password новый пароль
     * @param model модель веб-страницы
     * @return страницу восстановления пароля или перенаправление на страницу авторизации
     */
    @PostMapping("/resetPasswordAction")
    public String resetPasswordAction(@AuthenticationPrincipal User user,
                                      @ModelAttribute("token") String token,
                                      @ModelAttribute("password") String password,
                                      Model model) {
        return authorizationService.resetPasswordAction(user, token, password, model);
    }
}
