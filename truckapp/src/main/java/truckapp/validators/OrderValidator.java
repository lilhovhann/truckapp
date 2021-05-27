package truckapp.validators;

import truckapp.model.Employee;
import truckapp.model.Order;
import truckapp.model.Truck;
import truckapp.services.EmployeeService;
import truckapp.services.OrderService;
import truckapp.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Валидатор для проверки данных из формы оформления нового заказа
 *
 * @author kanenkovaa
 * @version 0.1
 */
@Component
public class OrderValidator implements Validator {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TruckService truckService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Order.class.equals(aClass);
    }

    /**
     * Метод, осуществляющий проверку данных из формы добавления нового заказа
     * @param o объект пользователя
     * @param errors лист для добавления ошибок
     */
    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "обязательно к заполнению");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "обязательно к заполнению");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "building", "обязательно к заполнению");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "targetDate", "обязательно к заполнению");
    }

    public void customValidate(Object o, int numberOfWorkers, List<Employee> workersBuf, Truck truck, Errors errors) {
        Order order = (Order) o;
//        List<Employee> workers = employeeService.findAll();
//        List<Employee> workersBuf = employeeService.setWorkersToOrder(order, workers);
        if(numberOfWorkers > workersBuf.size()) {
            errors.rejectValue("workers", "Не хватает свободных грузчиков");
        }
//        else {
//            order.setWorkers(workersBuf.subList(0, numberOfWorkers));
//        }
//        List<Truck> trucks = truckService.findAllByDescription(truckDescription);
        if (truck == null) {
            errors.rejectValue("truck", "Не хватает свободных автомобилей");
        }
//        else {
//            order.setTruck(truckService.setTruckToOrder(order, trucks));
//        }
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "обязательно к заполнению");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "обязательно к заполнению");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "building", "обязательно к заполнению");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "targetDate", "обязательно к заполнению");
        if (order.getAddressFrom().isEmpty() || order.getAddressTo().isEmpty())
            errors.rejectValue("addressFrom", "обязательно к заполнению");
        if (order.getTargetTime().length() != 5)
            errors.rejectValue("targetTime", "обязательно к заполнению");
        if (order.getTargetTime().length() == 5) {
            if (order.getTargetTime().split(":")[0].equals("20") && !order.getTargetTime().split(":")[1].equals("00"))
                errors.rejectValue("targetTime", "работаем до 20:00");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "targetDate", "обязательно к заполнению");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "distance", "обязательно к заполнению");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "duration", "обязательно к заполнению");
    }
}
