/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class MainTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void mainSemArgs() {
        exit.expectSystemExitWithStatus(1);
        Main.main(new String[0]);
    }

    @Test
    public void main1Arg() throws IOException, URISyntaxException {
        //cria arquivo local apenas pra testes//
        List<String> testeArq = new ArrayList<>();
        testeArq.add("a + b; a=1, b=2; 3");
        testeArq.add("5 / (6 - 5);;5");
        String diretorio = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath())
                .getParent();
        Path file = Paths.get(diretorio + "/teste.txt");
        Files.write(file, testeArq, Charset.forName("UTF-8"));

        //testes//
        exit.expectSystemExitWithStatus(0);
        Main.main(new String[]{diretorio + "/teste.txt"});

        //deleta o arquivo criado//
        Files.delete(file);
        file = Paths.get(diretorio + "/relatorio.json");
        Files.delete(file);
    }

    @Test
    public void main1ArgFileNotFound() throws IOException, URISyntaxException {
        exit.expectSystemExitWithStatus(1);
        Main.main(new String[]{"c:\\algumlugarquenao\\existemesmo\\teste.txt"});
    }

    @Test
    public void main2ArgsCertosLocal() throws IOException, URISyntaxException {
        //cria arquivo local apenas pra testes//
        List<String> testeArq = new ArrayList<>();
        testeArq.add("a + b; a=1, b=2; 3");
        testeArq.add("5 / (6 - 5);;5");
        String diretorio = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath())
                .getParent();
        Path file = Paths.get(diretorio + "/teste.txt");
        Files.write(file, testeArq, Charset.forName("UTF-8"));

        //testes//
        exit.expectSystemExitWithStatus(0);
        Main.main(new String[]{diretorio + "/teste.txt", "-h"});

        //deleta o arquivo criado//
        Files.delete(file);
        file = Paths.get(diretorio + "/relatorio.html");
        Files.delete(file);
    }

    @Test
    public void main2ArgsCertosOnline() throws URISyntaxException, IOException {
        exit.expectSystemExitWithStatus(0);
        Main.main(new String[]{"http://lennusoft.16mb.com/open/testes.txt",
            "-h"});

        //delete arquivo criado
        String diretorio = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath())
                .getParent();
        Path file = Paths.get(diretorio + "/relatorio.html");
        Files.delete(file);
    }

    @Test
    public void main2ArgsErrados() throws IOException, URISyntaxException {
        //cria arquivo local apenas pra testes//
        List<String> testeArq = new ArrayList<>();
        testeArq.add("a + b; a=1, b=2; 3");
        testeArq.add("5 / (6 - 5);;5");
        String diretorio = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath())
                .getParent();
        Path file = Paths.get(diretorio + "/teste.txt");
        Files.write(file, testeArq, Charset.forName("UTF-8"));

        //testes//
        exit.expectSystemExitWithStatus(1);
        Main.main(new String[]{diretorio + "/teste.txt", "-q"});

        //deleta o arquivo criado//
        Files.delete(file);
        file = Paths.get(diretorio + "/relatorio.html");
        Files.delete(file);
    }

}