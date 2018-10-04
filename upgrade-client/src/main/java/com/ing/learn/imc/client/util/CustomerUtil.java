package com.ing.learn.imc.client.util;

import com.ing.imc.domain.Customer;

import java.util.Random;


public class CustomerUtil {

    public static Customer generateRandomCustomer(String id) {
        return new Customer(id, getFirstName(getRandomBoolean()), getLastName());
    }

    private static boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    private static final String[] FIRST_NAMES_M = {"Louis","Arthur","Noah","Lucas","Liam","Adam","Victor","Jules","Mohamed","Nathan","Gabriel","Mathis","Hugo","Ethan","Thomas","Finn","Oscar","Rayan","Eden","Matteo","Vince","Alexander","Maxime","Théo","Stan","Leon","Aaron","Lars","Raphaël","Tom","David","Simon","Sacha","Luca","Milan","Yanis","Elias","Mats","Seppe","Nolan","Robin","Vic","William","Jack","Lewis","Alexandre","Samuel","Timéo","Tuur","Sam","Youssef","Clément","Daan","Felix","Imran","Maxim","Enzo","Martin","Léon","Wout","Emile","Kobe","Lowie","Ibrahim","Mathias","Senne","Léo","Warre","Amir","Jayden","Mathéo","Mathys","Ferre","Romain","Alexis","Diego","Emiel","Hamza","Ilyas","Alessio","Antoine","Mohammed","Cas","Ayoub","Baptiste","Marcel","Daniel","Tiago","Lou","Axel","Nicolas","Henri","Milo","Alex","Julien","Mauro","Loïc","Gust","Jasper","Maurice"};
    private static final String[] FIRST_NAMES_F = {"Emma","Louise","Olivia","Elise","Alice","Juliette","Mila","Lucie","Marie","Camille","Lina","Elena","Ella","Chloé","Anna","Léa","Sofia","Noor","Lena","Charlotte","Eva","Julie","Nina","Victoria","Zoé","Manon","Jade","Giulia","Nora","Sara","Clara","Lisa","Sarah","Amélie","Laura","Julia","Lotte","Lily","Mia","Fien","Lore","Elisa","Lola","Liv","Inaya","Anaïs","Nour","Luna","Axelle","Aya","Lou","Yasmine","Elsa","Jeanne","Amber","Eline","Lara","Maryam","Fleur","Inès","Emilie","Nore","Margaux","Rose","Amina","Ines","Pauline","Renée","Enora","Amira","Helena","Noémie","Valentina","Maya","Hanne","Roos","Alix","Maria","Alicia","Tess","Sophia","Elif","Marion","Estelle","Célia","Nell","Mona","Laure","Eléonore","Emilia","Margot","Zoë","Lilou","Emily","Jana","Fenna","Fatima","Kato","Leonie","Febe","Léna","Lise","Noa","Rosalie"};
    private static final String[] LAST_NAMES = {"Peeters","Janssens","Maes","Jacobs","Mertens","Willems","Claes","Goossens","Wouters","De Smet","Vermeulen","Pauwels","Dubois","Hermans","Aerts","Michiels","Lambert","Martens","De Vos","Smets","Dupont","Claeys","De Clercq","Desmet","Hendrickx","Van Damme","Stevens","De Backer","Janssen","Devos","Martin","Van De Velde","Segers","Lemmens","Coppens","Dumont","Wauters","Leroy","Van Den Broeck","Leclercq","Simon","De Cock","Verhoeven","Cools","Laurent","François","De Smedt","Declercq","Smet","Denis"};

    private static String getFirstName(boolean isFemale) {
        String[] nameArray = isFemale? FIRST_NAMES_F : FIRST_NAMES_M;
        return nameArray[randTo(nameArray.length)];
    }

    private static String getLastName() {
        return LAST_NAMES[randTo(LAST_NAMES.length)];
    }

    static int randTo(int end) {
        return new Random().nextInt(end);
    }

}
