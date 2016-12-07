/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Permite obter o arquivo com os testes, remotamente ou localmente.
 *
 * @author Wesleywrl
 */
public final class Ler {

    /**
     * Impede instancialização de classe utilitária.
     */
    private Ler() {
    }

    /**
     * Tenta obter o conteúdo de um arquivo de texto no computador ou em um
     * servidor.
     *
     * @param path Local do arquivo a ser obtido, local ou remoto (http).
     * @param local Se falso, o arquivo será obtido de um servidor remoto via
     * internet. Se verdade, o arquivo está no computador do usuário.
     * @return Lista com as linhas do arquivo obtido.
     * @throws java.io.FileNotFoundException Caso o arquivo não exista.
     * @throws java.net.MalformedURLException Caso a URL inserida for inválida.
     * @throws IOException Caso não for possível acessar a URL.
     */
    public static List<String> obterLinhas(final String path,
            final boolean local)
            throws FileNotFoundException, MalformedURLException, IOException {

        if (local) {
            return obterLinhasLocal(path);
        } else {
            return obterLinhasOnline(path);
        }

    }

    /**
     * Acessa uma URL e retorna as linhas do arquivo acessado.
     *
     * @param urlPath Endereço URL do arquivo a ser obtido.
     * @return Todas as linhas do arquivo acessado.
     * @throws MalformedURLException Quando a URL inserida for inválida.
     * @throws IOException Quando não for possível conectar-se ao servidor.
     */
    private static List<String> obterLinhasOnline(final String urlPath)
            throws MalformedURLException, IOException {

        List<String> linhas = new ArrayList<>();
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (Scanner leitor = new Scanner(
                new InputStreamReader(connection.getInputStream()))) {
            while (leitor.hasNextLine()) {
                linhas.add(leitor.nextLine());
            }
        }

        return linhas;
    }

    /**
     * Acessa um arquivo local e retorna as linhas do arquivo.
     *
     * @param path Diretório do arquivo local.
     * @return Todas as linhas do arquivo acessado.
     * @throws FileNotFoundException Quando o arquivo não existir.
     */
    private static List<String> obterLinhasLocal(final String path)
            throws FileNotFoundException {

        List<String> linhas = new ArrayList<>();

        try (Scanner leitor = new Scanner(new FileReader(path))) {
            while (leitor.hasNextLine()) {
                linhas.add(leitor.nextLine());
            }
        }

        return linhas;
    }

}
