package com.example.airports;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

@SpringBootApplication
public class AirportsApplication implements CommandLineRunner {
    @Value("${app.parseColumn}")
    int columnDefault;

    @Value("${app.table.line.size}")
    int lineSize;

    @Value("${app.filePath}")
	String filePath;

    public static void main(String[] args) {
        SpringApplication.run(AirportsApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        var column = columnDefault;
        if (args.length > 0) {
            try {
                column = parseInt(args[0]);
            } catch (NumberFormatException e) {
                out.println("ваш аргумент не подходит под номер колонки");
                return;
            }
            if (column < 0 || column >= lineSize) {
                out.println("нет такой колонки");
                return;
            }
        }
		FileReader fileReader;
        try {
			fileReader = new FileReader(filePath);
		}
        catch (FileNotFoundException e){
        	e.printStackTrace();
        	out.println("не найден файл airports.dat");
        	return;
		}
		CSVReader reader = new CSVReader(fileReader, ',', '"', 1);
        Trie trie = new Trie();
        String[] nextLine;
        while ( (nextLine = reader.readNext()) != null) {
            trie.put(nextLine, column);
        }
        reader.close();
        Scanner in = new Scanner(System.in);
        System.out.print("Введите строку: ");
        String inputText = in.nextLine();
        long start = System.currentTimeMillis();
        var resLines = trie.getAllLeafWithStart(inputText);
        long end = System.currentTimeMillis();
        for (var line : resLines
        ) {
            out.println(Arrays.toString(line));
        }
        out.printf("Количество найденных строк: %d%n", resLines.size());
        out.printf("Время, затраченное на поиск: %d мс%n", end - start);
    }
}
