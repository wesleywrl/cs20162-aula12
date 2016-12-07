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
import org.junit.Test;

public class RelatorioTesteTest {

    @Test
    public void instancia() {
        List<String> linhas = new ArrayList<>();
        RelatorioTeste relat = new RelatorioTeste(linhas, false);
    }

    @Test
    public void relatorioHtml() throws IOException, URISyntaxException {
        //cria arquivo local apenas pra testes//
        List<String> testeArq = new ArrayList<>();
        testeArq.add("a + b; a=1, b=2; 3");
        testeArq.add("5 / (6 - 5);;5");
        testeArq.add("5 / (6 - 5);;4");
        String diretorio = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath())
                .getParent();
        Path file = Paths.get(diretorio + "/teste.txt");
        Files.write(file, testeArq, Charset.forName("UTF-8"));

        //gera o relatorio
        List<String> linhas = testeArq;
        RelatorioTeste relat = new RelatorioTeste(linhas, true);
        relat.gerarRelatorioTeste(diretorio);

        //Deleta os arquivos gerados
        file = Paths.get(diretorio + "/relatorio.html");
        Files.delete(file);
        file = Paths.get(diretorio + "/teste.txt");
        Files.delete(file);
    }

    @Test
    public void relatorioJson() throws IOException, URISyntaxException {
        //cria arquivo local apenas pra testes//
        List<String> testeArq = new ArrayList<>();
        testeArq.add("a + b; a=1, b=2; 3");
        testeArq.add("5 / (6 - 5);;5");
        testeArq.add("5 / (6 - 5);;4");
        String diretorio = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath())
                .getParent();
        Path file = Paths.get(diretorio + "/teste.txt");
        Files.write(file, testeArq, Charset.forName("UTF-8"));

        //gera o relatorio
        List<String> linhas = testeArq;
        RelatorioTeste relat = new RelatorioTeste(linhas, false);
        relat.gerarRelatorioTeste(diretorio);

        //Deleta os arquivos gerados
        file = Paths.get(diretorio + "/relatorio.json");
        Files.delete(file);
        file = Paths.get(diretorio + "/teste.txt");
        Files.delete(file);
    }
}
