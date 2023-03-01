package HM_Order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        boolean next = true;
        FileWriter fileWriter = null;

        try {
            Scanner scanner = new Scanner(System.in);
            fileWriter = new FileWriter("orderInfo.txt", true);
            while (next) {
                Order order = new Order();
                System.out.print("Введите наименование продукта: ");
                order.setName(scanner.nextLine());

                System.out.print("Введите количество: ");
                order.setQuantity(scanner.nextInt());

                System.out.print("Введите стоимость: ");
                order.setCost(scanner.nextDouble());
                scanner.nextLine();
                String orderInfo = order.toString();

                fileWriter.write(orderInfo);
                logger.info("Заказ сохранен в файл: " + orderInfo);
                System.out.print("Вы хотите продолжить заказ? (да/нет): ");
                String answer = scanner.nextLine();
                next = answer.equalsIgnoreCase("да");
            }
        } catch (IOException e) {
            logger.error("Ошибка сохранения заказа в файл: " + e.getMessage());
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    logger.error("Ошибка закрытия FileWriter: " + e.getMessage());
                }
            }
        }
    }
}
