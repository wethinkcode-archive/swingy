package com.swingy.controller;

//import com.swingy.model.GameModel;
import com.swingy.model.GameModel;
import com.swingy.model.character.CharacterType;
import com.swingy.model.character.heros.Hero;
import com.swingy.utils.database.DatabaseWrapper;
import com.swingy.model.character.CharacterFactory;
import com.swingy.utils.factory.MapFactory;
import com.swingy.view.console.ConsoleView;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static com.swingy.utils.Colors.*;
import static com.swingy.utils.Log.inputSign;
import static com.swingy.utils.Log.log;
import static com.swingy.utils.StaticGlobal.*;

public class ConsoleController extends GameController {

    public static void selectHeroType() {
        Scanner scanner = getScanner();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")) {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        ConsoleView.showHeroNameOption(CharacterType.DEADPOOL);
                        break;
                    case 2:
                        ConsoleView.showHeroNameOption(CharacterType.THOR);
                        break;
                    case 3:
                        ConsoleView.showHeroNameOption(CharacterType.WOLVERINE);
                        break;
                    case 4:
                        ConsoleView.goodbye();
                        break;
                }
                break;
            } else {
//                TODO refactor
                log(ANSI_RED + ":::ERROR::: Hero Type Does Not Exist, Try Again!" + ANSI_RESET);
                inputSign();
            }
        }
        scanner.close();
    }


    public static void createHero(CharacterType type) {
        Scanner scanner = getScanner();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if (input.length() >= 2 && input.length() < 10) {
                try {
                    // Create the hero and store it in the database.
                    if (!DatabaseWrapper.getInstance().heroExists(input)) {
                        DatabaseWrapper.getInstance().insertHero(CharacterFactory.newHero(input.trim(), type));
//                        log(ANSI_CYAN + "Created Hero Named: " + ANSI_YELLOW + input + ANSI_RESET);
                        hero = DatabaseWrapper.getInstance().retrieveHeroData(input.trim());
                        map = MapFactory.generateMap(hero);
                        ConsoleView.showSelectedHero(hero, map.getSize());
                        startMission();
//                        ConsoleView.displayMoveList();

                    } else {
                        //                TODO refactor
                        log(ANSI_RED + " :::ERROR::: " + input + " Hero Exists, Try again!" + ANSI_RESET);
                        inputSign();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
//                catch (SQLException | IOException | ClassNotFoundException exception) {
//                    exception.printStackTrace();
//                }
//                break;
            } else if (input.length() < 3 || input.length() > 10) {
                //                TODO refactor
                log(ANSI_RED + ":::ERROR::: Name Must Have 2 - 10 Characters, Try Again!" + ANSI_RESET);
                inputSign();
            }
        }
        scanner.close();
    }

//   TODO Refactor
    public static void selectExistingHero() {
        try {
            // First check if there are heroes in the database.
            int count = DatabaseWrapper.getInstance().numberOfHeroes();
            if (count > 0) {
                ConsoleView.heroCount(count);
                // Display all the available heroes in the database.
                List<Hero> heros = DatabaseWrapper.getInstance().retrieveAllHeroes();
                ConsoleView.showAvailableHeros(heros);
            } else {
                //                TODO refactor
                log(ANSI_RED + ":::ERROR::: No Heroes Available!" + ANSI_RESET);
                inputSign();
                ConsoleView.showMainOptions();
            }
        } catch (SQLException exception) {
            log(ANSI_RED + ":::ERROR::: No Heroes Available!" + ANSI_RESET);
            inputSign();
        }
//        TODO refactor selectedHero()
        selectedHero();
    }

    private static void selectedHero() {
        Scanner scanner = getScanner();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            try {
                // Check if the specified character name exist in the database,
                // If the character name exist in the database, retrieve the data to character object,
                // And lastly generate the map.
                if (DatabaseWrapper.getInstance().heroExists(input.trim())) {
//                    TODO: refactor
                    log(ANSI_YELLOW + ":::" + ANSI_RESET + CYAN_BOLD_BRIGHT + "Let's play" + ANSI_RESET + ANSI_YELLOW  + ANSI_RESET);
                    hero = DatabaseWrapper.getInstance().retrieveHeroData(input);
//                    TODO check if the hero is not null
                    map = MapFactory.generateMap(hero);
                    ConsoleView.showSelectedHero(hero, map.getSize());

                    startMission();
                } else {
                    //                TODO refactor
                    log(ANSI_RED + ":::ERROR::: Hero Does not Exist, Try Again!!" + ANSI_RESET);
                    inputSign();
                }
            } catch (SQLException exception) {
                log(ANSI_RED + ":::ERROR::: Something went wrong, please try again" + ANSI_RESET);
                inputSign();
            }
        }
    }

    //    TODO refactor
    public static void startMission() {
        Scanner scanner = getScanner();

        ConsoleView.showDirectionOptions();
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine().trim();
                int direction = Integer.parseInt(line);
                if (direction == 1 || direction == 2 ||
                        direction == 3 || direction == 4) {
                    GameModel.moveHero(direction);
                    GameModel.goal();
                    ConsoleView.showSelectedHero(hero, map.getSize());
                    ConsoleView.showDirectionOptions();
                } else if (direction == 5){
                    ConsoleView.goodbye();
                } else {
                    //                TODO refactor
                    log(ANSI_RED + ":::ERROR::: Incorrect choice, please choose between (1-4). Try Again!" + ANSI_RESET);
                    inputSign();
                }
            } catch (Exception e) {
                //                TODO refactor
                log(ANSI_RED + ":::ERROR::: Incorrect choice, please choose between (1-4). Try Again!" + ANSI_RESET);
                inputSign();
            }
        }
        scanner.close();
    }

    public static void startGame() {
        Scanner scanner = getScanner();

        while(scanner.hasNextLine()) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                ConsoleView.showMainOptions();
            } else if (input.equals("n") || input.equals("no")) {
                log(ANSI_RED + ":::" + "SCARED?::: Go drink some water and try again later" + ANSI_RESET);
                System.exit(-1);
            } else {
            //               TODO refactor
                log(ANSI_RED + ":::ERROR::: Expected input (Y)es or (N)o, Try Again!!!" + ANSI_RESET);
                inputSign();
            }

        }
        scanner.close();
    }

    private static Scanner getScanner() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("$ ");
        return scanner;
    }

    public static void selectMainOption() {
        Scanner scanner = getScanner();

        while(scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equals("1") || input.equals("2")
                    || input.trim().equals("3")) {
                int option = Integer.parseInt(input);
                switch (option) {
                    case 1:
                        ConsoleView.showHeroType();
                        break;
                    case 2:
                        ConsoleView.showExistingHero();
                        break;
                    case 3:
//                        TODO: goodbye message
                        ConsoleView.goodbye();
                        break;
                }
            } else {
                //                TODO refactor
                log(ANSI_RED + ":::ERROR::: Incorrect choice, please choose between (1-3). Try Again!" + ANSI_RESET);
                inputSign();
            }
        }
        scanner.close();
    }

    public static void retry() {
        Scanner scanner = getScanner();

        while(scanner.hasNext()) {
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("y") || input.toLowerCase().equals("yes")) {
//                todo
                ConsoleView.showMainOptions();
            } else if (input.toLowerCase().equals("n") || input.toLowerCase().equals("no")){
//                todo
               ConsoleView.goodbye();
            } else {
                log(ANSI_RED + ":::ERROR::: Expected input (Y)es or (N)o, Try Again!!!" + ANSI_RESET);
                inputSign();
            }
        }
    }

    /**
     * Equip the character with the chosen artifact.
     * The character wont be equiped if the Experience
     * is chosen instead of an Artifact.
     *
     * @param artifactType The artifact type.
     */
    public static void equip(String artifactType) {
        if (CONSOLE_MODE) {
            Scanner scanner = new Scanner(System.in);
            log(ANSI_YELLOW + "::: Do You Wanna Keep The Artifact?\n1. YES!\n2. NO!" + ANSI_RESET);
            inputSign();
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equals("1") || input.equals("2")) {
                    int choice = Integer.parseInt(input.trim());
                    if (choice == 1) {
                        hero.equipHero(artifact, artifact.getType());
                        log("::: " + hero.getName() + " Is Equipped With " + artifactType);
                        break;
                    } else if (choice == 2) {
                        break;
                    }
                } else {
                    //                TODO refactor
                    log(ANSI_RED + ":::ERROR::: Incorrect Choice, Try Again!" + ANSI_RESET);
                    inputSign();
                }
            }
        }
    }


    /**
     * When a character moves to a position occupied by
     * an enemy, the character has two options.
     *
     * 1. Fight: {@code fight()}, which engages him in
     * a battle with the enemy.
     *
     * 2. Run: {@code run()},  which gives him a 50% chance
     * of returning to the position. If the odds arent on his side,
     * he must fight the eneny.
     */
    public static void action() {
        Scanner scanner = new Scanner(System.in);

        log(ANSI_YELLOW + "::: You Are Facing: " + villian.getName() + ANSI_RESET);
        ConsoleView.showActionOption();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            int choice = Integer.parseInt(input);

            if (choice == 1 || choice == 2) {
                switch (choice) {
                    case 1:
                        fight();
                        return;
                    case 2:
                        GameModel.run();
                        return;
                    default:
                        break;
                }
            } else {
                //                TODO refactor
                log(ANSI_RED + ":::ERROR::: Incorrect choice, please choose between (1-2). Try Again!\n" + ANSI_RESET);
                inputSign();
//                log("Try again!");
                ConsoleView.showActionOption();
            }
        }

    }
}