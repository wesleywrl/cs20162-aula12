/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */
package com.github.wesleywrl.qp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe capaz de executar uma sequência de testes de Parser e gerar um
 * relatório HTML ou JSON.
 *
 * @author Wesleywrl
 */
public class RelatorioTeste {

    /**
     * Valor para conversão de taxas em porcentagens (ex.: a taxa 0.5 vira 50
     * porcento).
     */
    private static final float RATE_TO_PERCENT = 100f;

    /**
     * Todos os testes deste relatório.
     */
    private final List<Teste> testes = new ArrayList<>();

    /**
     * Se verdadeiro, será salvo um arquivo HTML. Se falso, será salvo um JSON.
     */
    private final boolean gerarHtml;

    /**
     * Tempo total gasto na realização das expressões.
     */
    private float tempoTotal;

    /**
     * Tempo gasto em média por expressão.
     */
    private float tempoMedio;

    /**
     * Memória consumida durante a execução dos testes.
     */
    private float memoriaConsumida;

    /**
     * Constrói uma nova classe de relatório com a configuração do tipo de
     * relatório e as linhas dos testes a serem executados.
     *
     * @param linhasTestes Linhas dos testes a serem executados.
     * @param html Se verdadeiro, o relatório será HTML. Se falso, o relatório
     * será JSON.
     */
    public RelatorioTeste(final List<String> linhasTestes, final boolean html) {
        //Ao instanciar a classe Relatório, já transforma-se todas as linhas de
        //teste fornecida em classes Teste propriamente ditas. Neste processo,
        //cada Teste já tem seus atributos separados (expressão, variáveis e
        //valor esperado). Este processo é antes da medição de tempo e memória
        //pois não é de responsabilidade do Parser. Isto se dá pois o Parser só
        //deve receber a expressão e as variáveis já separadas.
        linhasTestes.stream().forEach((linha) -> {
            testes.add(new Teste(linha));
        });
        this.gerarHtml = html;
    }

    /**
     * Gera o arquivo HTML ou JSON com o relatório dos testes atuais. O
     * relatório mostra os testes que foram executados e o resultado de cada um.
     * Além disso, também a quantidade de testes rodados, a porcentagem de
     * falhas, o tempo total gasto pelo Parser, o tempo médio (por testes) e a
     * memória necessária para a execução.
     *
     * @param diretorio Diretório local em que o relatório será salvo.
     * @throws java.io.IOException Quando não for possível guardar o arquivo no
     * diretório fornecido.
     */
    public final void gerarRelatorio(final String diretorio)
            throws IOException {

        //Obtém tempo e memória iniciais
        long inicio = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memInicio = runtime.totalMemory() - runtime.freeMemory();

        //Executa e obtém os resultados das expressões a serem testadas
        realizarExpressoes();

        //Obtém tempo e memória finais
        tempoTotal = System.currentTimeMillis() - inicio;
        tempoMedio = tempoTotal / testes.size();
        runtime = Runtime.getRuntime();
        runtime.gc();
        long memFinal = runtime.totalMemory() - runtime.freeMemory();
        memoriaConsumida = memFinal - memInicio;

        //Atualiza a condição de sucesso dos testes
        testes.stream().forEach((teste) -> {
            teste.atualizarSucesso();
        });

        //Finalmente, gera o arquivo com o relatório dos testes
        gerarArquivo(diretorio);
    }

    /**
     * Usa o Parser para realizar todas as expressoes inseridas (e já
     * destrinchados) e guarda o resultado de cada uma.
     */
    private void realizarExpressoes() {
        testes.stream().forEach((teste) -> {
            teste.calcularValor();
        });
    }

    /**
     * Gera o arquivo do relatório, determinando se será em JSON ou HTML.
     *
     * @param diretorio Local em que o arquivo deverá ser salvo.
     * @throws IOException Quando não foi possível salvar o arquivo.
     */
    private void gerarArquivo(final String diretorio) throws IOException {
        if (gerarHtml) {
            gerarArquivoHtml(diretorio);
        } else {
            gerarArquivoJson(diretorio);
        }
    }

    /**
     * Cria um arquivo HTML padrão para o relatório, e o salva no diretório
     * deste programa.
     *
     * @param diretorio Local em que o arquivo deverá ser salvo.
     * @throws IOException Quando não é possível salvar o arquivo HTML.
     */
    private void gerarArquivoHtml(final String diretorio) throws IOException {
        List<String> arquivo = new ArrayList<>();

        //Cabeçalho e início do relatório
        arquivo.add("<!DOCTYPE html>");
        arquivo.add("<html>");
        arquivo.add("");
        arquivo.add("<head>");
        arquivo.add("<meta charset=\"UTF-8\">");
        arquivo.add("<title>Relatório de Testes</title>");
        arquivo.add("<style>th, td {padding: 5px;"
                + "border-bottom: 1px solid #ddd;}</style>");
        arquivo.add("</head>");
        arquivo.add("");
        arquivo.add("<body>");
        arquivo.add("<h1>Relatório de testes do Parser</h1>");
        arquivo.add("");

        //Informações gerais
        int totTestes = testes.size();
        int failTestes = 0;
        failTestes = testes.stream().filter((teste) -> (!teste.getSucesso())).
                     map((_item) -> 1).reduce(failTestes, Integer::sum);
        float failRate = (float) failTestes / (float) totTestes;
        arquivo.add("<h2>Informações gerais</h2>");
        arquivo.add("<p><b>Testes executados:</b> " + totTestes + " testes.");
        arquivo.add("</br><b>Testes que falharam:</b> " + failTestes
                + " testes (" + String.format("%.0f", failRate
                        * RATE_TO_PERCENT) + "%).");
        arquivo.add("</br><b>Tempo total:</b> "
                + String.format("%.0f", tempoTotal) + " milisegundos.");
        arquivo.add("</br><b>Tempo médio:</b> "
                + String.format("%.3f", tempoMedio)
                + " milisegundos por teste.");
        arquivo.add("</br><b>Memória consumida:</b> "
                + String.format("%.2f", memoriaConsumida) + " bytes.");
        arquivo.add("</br><small>Note que para arquivos de teste pequenos,"
                + " a memória consumida pode não ser precisamente detectada."
                + "</small>");
        arquivo.add("</p>");
        arquivo.add("");

        //Testes um a um
        arquivo.add("<h2>Testes detalhados</h2>");
        arquivo.add("<table>");
        arquivo.add("<tr>");
        arquivo.add("<th><b>Expressão</b></th>");
        arquivo.add("<th><b>Variáveis</b></th>");
        arquivo.add("<th><b>Esperado</b></th>");
        arquivo.add("<th><b>Obtido</b></th>");
        arquivo.add("<th><b>Sucesso</b></th>");
        arquivo.add("</tr>");
        testes.stream().map((teste) -> {
            arquivo.add("<tr>");
            arquivo.add("<td>" + teste.getExpressao() + "</td>");
            return teste;
        }).map((teste) -> {
            arquivo.add("<td>" + teste.getVariaveis() + "</td>");
            return teste;
        }).map((teste) -> {
            arquivo.add("<td>" + String.format("%.4f", teste.getEsperado())
                    + "</td>");
            return teste;
        }).map((teste) -> {
            arquivo.add("<td>" + String.format("%.4f", teste.getObtido())
                    + "</td>");
            return teste;
        }).map((teste) -> {
            if (teste.getSucesso()) {
                arquivo.add("<td><span style=\"color:#00FF00\">SIM</span>"
                        + "</td>");
            } else {
                arquivo.add("<td><span style=\"color:#FF0000\">NÃO</span>"
                        + "</td>");
            }
            return teste;
        }).forEach((_item) -> {
            arquivo.add("</tr>");
        });
        arquivo.add("</table>");
        arquivo.add("");
        if (todosSucessos()) {
            arquivo.add("<b><span style=\"color:#00FF00\">Todos os testes "
                    + "passaram.</span></b>");
        } else {
            arquivo.add("<b><span style=\"color:#FF0000\">Os testes não foram "
                    + "executados com sucesso.</span></b>");
        }
        arquivo.add("");
        arquivo.add("</body>");
        arquivo.add("");
        arquivo.add("</html>");

        //Cria e salva o arquivo
        Path file = Paths.get(diretorio + "/relatorio.html");
        Files.write(file, arquivo, Charset.forName("UTF-8"));
    }

    /**
     * Cria um arquivo JSON padrão para o relatório, e o salva no diretório
     * deste programa.
     *
     * @param diretorio Local em que o relatório deve ser salvo.
     * @throws IOException Quando não é possível salvar o arquivo JSON.
     */
    private void gerarArquivoJson(final String diretorio) throws IOException {
        List<String> arquivo = new ArrayList<>();

        //Informações gerais
        int totTestes = testes.size();
        int failTestes = 0;
        failTestes = testes.stream().filter((teste) -> (!teste.getSucesso())).
                     map((_item) -> 1).reduce(failTestes, Integer::sum);
        arquivo.add("{");
        arquivo.add("    \"testesTotais\":" + totTestes + ",");
        arquivo.add("    \"testesFalhos\":" + failTestes + ",");
        arquivo.add("    \"tempoTotal\":" + tempoTotal + ",");
        arquivo.add("    \"tempoMedio\":" + tempoMedio + ",");
        arquivo.add("    \"memoriaConsumida\":" + memoriaConsumida + ",");

        //Testes
        arquivo.add("    \"testes\":[");
        testes.stream().forEach((Teste teste) -> {
            String virgula = ",";
            if (testes.indexOf(teste) == testes.size() - 1) {
                virgula = "";
            }
            arquivo.add("        {");
            arquivo.add("            \"expressao\":\"" + teste.getExpressao()
                    + "\",");
            if (teste.temVariaveis()) {
                arquivo.add("            \"variaveis\":[");
                for (int i = 0; i < teste.getVariaveisNome().length; i++) {
                    String virgulaVar = ",";
                    if (i == teste.getVariaveisNome().length - 1) {
                        virgulaVar = "";
                    }
                    arquivo.add("                {");
                    arquivo.add("                    \""
                            + teste.getVariaveisNome()[i] + "\":"
                            + teste.getVariaveisValor()[i]);
                    arquivo.add("                }" + virgulaVar);
                }
                arquivo.add("            ],");
                
            }
            arquivo.add("            \"esperado\":" + teste.getEsperado()
                    + ",");
            arquivo.add("            \"obtido\":" + teste.getObtido() + ",");
            arquivo.add("            \"sucesso\":" + teste.getSucesso());
            arquivo.add("        }" + virgula);
        });
        arquivo.add("    ]");
        arquivo.add("}");

        //Cria e salva o arquivo
        Path file = Paths.get(diretorio + "/relatorio.json");
        Files.write(file, arquivo, Charset.forName("UTF-8"));
    }

    /**
     * Verifica se todos os testes foram executados corretamente. Deve ser
     * chamado após a execução de "realizarExpressoes".
     *
     * @return Falso se pelo menos um teste tiver dado errado.
     */
    public final boolean todosSucessos() {
        return testes.stream().noneMatch((teste) -> (!teste.getSucesso()));
    }

}