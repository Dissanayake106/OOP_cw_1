import java.util.InputMismatchException;
import java.util.Scanner;

public class SignIn {
    public void signIn() {
        DatabaseConnector authManager = new DatabaseConnector();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println("=                                Welcome to \"World Wire\" News Application                       =");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println(" ");
        System.out.println(centerText("====> INTRODUCTIONS <====", 96));
        System.out.println("01. If you are a new user, Type \"1\" to Sign Up");
        System.out.println("02. If you already have an account, Type \"2\" to Login");
        System.out.println("03. For Age please enter a valid integer");
        System.out.println("04. The Username must be at least 5 characters long, containing only letters and numbers");
        System.out.println("05. Please enter a valid email in lowercase letters, following the format 'username@gmail.com'");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println(" ");

        int choice = 0;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("=> Select, Sign Up or Login : ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1 || choice == 2) {
                    validChoice = true;
                } else {
                    System.out.println(centerText(">>> Invalid choice. Please enter 1 for Sign Up or 2 for Sign In <<<", 96));
                    System.out.println(" ");
                }
            } catch (InputMismatchException e) {
                System.out.println(centerText(">>> Invalid input. Please enter 1 for Sign Up or 2 for Login <<<", 96));
                System.out.println(" ");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        if (choice == 1) {
            // Sign-Up Process for Regular User
            boolean signUpSuccessful = false;
            while (!signUpSuccessful) {
                System.out.println(centerText("====> Sign UP <====", 96));
                System.out.println(" ");
                String firstName = "";
                String lastName = "";

                while (true) {
                    System.out.print("=> Enter First Name: ");
                    firstName = scanner.nextLine().trim();
                    if (isAlpha(firstName)) {
                        break;
                    } else {
                        System.out.println(centerText(">>> Invalid input for first name. Please enter only alphabetical characters <<<", 96));
                        System.out.println(" ");
                    }
                }

                while (true) {
                    System.out.print("=> Enter Last Name: ");
                    lastName = scanner.nextLine().trim();
                    if (isAlpha(lastName)) {
                        break;
                    } else {
                        System.out.println(centerText(">>> Invalid input for last name. Please enter only alphabetical characters <<<", 96));
                        System.out.println(" ");
                    }
                }

                int age = 0;
                boolean validAge = false;
                while (!validAge) {
                    System.out.print("=> Enter Age: ");
                    try {
                        age = scanner.nextInt();
                        scanner.nextLine(); // Clear the newline character
                        validAge = true;
                    } catch (InputMismatchException e) {
                        System.out.println(centerText(">>> Invalid input for age. Please enter a valid integer <<<", 96));
                        System.out.println(" ");
                        scanner.nextLine(); // Clear the invalid input
                    }
                }

                String userName;
                while (true) {
                    System.out.print("=> Enter Username : ");
                    userName = scanner.nextLine().trim();
                    if (isValidUsername(userName)) {
                        break;
                    } else {
                        System.out.println(centerText(">>> Invalid username. It must be at least 5 characters long and contain only letters and numbers <<<", 96));
                        System.out.println(" ");
                    }
                }

                String email;
                while (true) {
                    System.out.print("=> Enter Email : ");
                    email = scanner.nextLine().trim();
                    if (isValidEmail(email)) {
                        break;
                    } else {
                        System.out.println(centerText(">>> Invalid email format. Please enter an email in lowercase letters following 'username@gmail.com' <<<", 96));
                        System.out.println(" ");
                    }
                }

                System.out.print("=> Enter Password: ");
                String password = scanner.nextLine();

                User user = new User(firstName, lastName, age, userName, email, password);

                if (authManager.signUp(user)) {
                    System.out.println(" ");
                    System.out.println(centerText(">>> Sign-Up Successful! <<<", 96));
                    signUpSuccessful = true;

                    // Prompt the user to log in after sign-up
                    boolean validResponse = false;
                    while (!validResponse) {
                        System.out.print("=> Do you want to log in now? (yes/no): ");
                        String response = scanner.nextLine().trim().toLowerCase();

                        if (response.equals("yes")) {
                            validResponse = true;
                            boolean loggedIn = false;
                            while (!loggedIn) {
                                System.out.println(centerText("====> Login <====", 96));
                                System.out.println(" ");
                                System.out.print("=> Enter Username: ");
                                String userNameLogin = scanner.nextLine();

                                System.out.print("=> Enter Password: ");
                                String passwordLogin = scanner.nextLine();

                                String loginMessage = authManager.signIn(userNameLogin, passwordLogin);
                                if (loginMessage.contains("Successful")) {
                                    loggedIn = true;
                                    System.out.println(" ");
                                    System.out.println(centerText(">>> " + loginMessage + " <<<", 96));

                                    // Check if the user logged in as an Admin
                                    if (loginMessage.contains("Admin")) {
                                        Admin_Page adminPage = new Admin_Page();
                                        adminPage.adminPage(userNameLogin);
                                    }

                                    if (loginMessage.contains("User")) {
                                        // If login is successful as User, go to the User Page
                                        User_Page userPage = new User_Page();
                                        userPage.userPage(userNameLogin);  // Display the User Interface
                                    }

                                } else {
                                    System.out.println(" ");
                                    System.out.println(centerText(">>> Invalid credentials, please try again <<<", 96));
                                    System.out.println(" ");
                                }
                            }
                        } else if (response.equals("no")) {
                            validResponse = true;
                            System.out.println(centerText(">>> You can log in anytime later. <<<", 96));
                        } else {
                            System.out.println(centerText(">>> Invalid response. Please enter 'yes' or 'no'. <<<", 96));
                            System.out.println(" ");
                        }
                    }
                } else {
                    System.out.println(" ");
                    System.out.println(centerText(">>> Sign-Up failed! Username or Email may already be taken. Please try again. <<<", 96));
                }
            }
        } else {
            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.println(centerText("====> Login <====", 96));
                System.out.println(" ");
                System.out.print("=> Enter Username: ");
                String userName = scanner.nextLine();

                System.out.print("=> Enter Password: ");
                String password = scanner.nextLine();

                String loginMessage = authManager.signIn(userName, password);
                if (loginMessage.contains("Successful")) {
                    loggedIn = true;
                    System.out.println(" ");
                    System.out.println(centerText(">>> " + loginMessage + " <<<", 96));

                    // Check if the user logged in as an Admin
                    if (loginMessage.contains("Admin")) {
                        Admin_Page adminPage = new Admin_Page();
                        adminPage.adminPage(userName);
                    }

                    if (loginMessage.contains("User")) {
                        // If login is successful as User, go to the User Page
                        User_Page userPage = new User_Page();
                        userPage.userPage(userName);  // Display the User Interface
                    }

                } else {
                    System.out.println(" ");
                    System.out.println(centerText(">>> Invalid credentials, please try again <<<", 96));
                    System.out.println(" ");
                }
            }
        }
    }

    private boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 5 && username.matches("[a-zA-Z0-9]+");
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-z0-9]+@[a-z0-9]+\\.[a-z]+");
    }

    private String centerText(String text, int length) {
        int padding = (length - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}