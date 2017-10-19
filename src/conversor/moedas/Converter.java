/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversor.moedas;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Classe responsável pela conversão das moedas
 *
 * @author klaus
 */
public class Converter {

    /* Paridade para compra de moedas */
    private static final int PARIDADE_COMPRA = 6;
    /* Taxa para compra de moedas */
    private static final int TAXA_COMPRA = 4;
    /* Tipo da moeda */
    private static final int TIPO_MOEDA = 2;

    /**
     *
     * @param from Moeda de origem
     * @param to Moeda de destino
     * @param value Valor a ser convertido
     * @param quotation Data da cotação da moeda
     *
     * @return BigDecimal
     * @throws Exception Problema na conversão da moeda
     */
    public BigDecimal currencyQuotation(String from, String to, Number value, String quotation) throws Exception {
        // Cria map com as informações das moeda
        Map<String, String[]> data = getCsvData(quotation);
        // Retorna dados da moeda de origem
        String[] fromData = data.get(from);
        // Retorna dados da moeda de destino
        String[] toData = data.get(to);
        BigDecimal result = new BigDecimal(0);
        // Verifica se existe dados para as moedas seleciondas ou alguma delas é BRL
        if ((fromData == null && !from.equals("BRL")) || (toData == null && !to.equals("BRL"))) {
            throw new Exception("Os parâmetros de moedas passados não existem");
        }
        // Caso o valor digitado seja inválido retorna erro
        if (value.floatValue() <= 0) {
            throw new Exception("O valor digitado não pode ser menor ou igual a zero");
        }
        // Verfica se a moeda desejada é REAL
        if (from.equals("BRL") || to.equals("BRL")) {
            result = calcCurrencyBrl(fromData, toData, value);
        } else {
            // Realiza calculo da conversão de valores
            result = calcCurrency(fromData, toData, value);
        }
        return result;
    }

    /**
     * Calcula o valor convertido
     *
     * @param fromData Dados da moeda de origem
     * @param toData Dados da moeda de destino
     * @param montante Montante convertido
     * @return BigDecimal
     */
    private BigDecimal calcCurrency(String[] fromData, String[] toData, Number montante) {
        // Paridade do destino
        BigDecimal toParidade = new BigDecimal(formatNumberDecimal(toData[PARIDADE_COMPRA]));
        // Paridade da origem
        BigDecimal fromParidade = new BigDecimal(formatNumberDecimal(fromData[PARIDADE_COMPRA]));
        // Montante digitado para conversão
        BigDecimal nMontante = new BigDecimal(montante.floatValue());
        BigDecimal value = new BigDecimal(0);
        // Verifica se a moeda de origem é do tipo A
        if (isCoinTypeA(fromData)) {
            value = nMontante.divide(fromParidade, 10, RoundingMode.DOWN);
        } else {
            value = nMontante.multiply(fromParidade);
        }
        // Verifica se a moeda de destino é do tipo A
        if (isCoinTypeA(toData)) {
            value = value.multiply(toParidade);
        } else {
            value = value.divide(toParidade, 10, RoundingMode.DOWN);
        }
        return value.setScale(2, RoundingMode.DOWN);
    }

    /**
     * Calcula o valor convertido
     *
     * @param fromData Dados da moeda de origem
     * @param toData Dados da moeda de destino
     * @param montante Montante convertido
     * @return BigDecimal
     */
    private BigDecimal calcCurrencyBrl(String[] fromData, String[] toData, Number montante) {
        BigDecimal value = new BigDecimal(0);
        // Verifica se existe valor de origem
        if (fromData != null) {
            // Taxa de origem
            BigDecimal fromTaxa = new BigDecimal(formatNumberDecimal(fromData[TAXA_COMPRA]));
            // Montante digitado para conversão
            BigDecimal nMontante = new BigDecimal(montante.floatValue());
            value = nMontante.multiply(fromTaxa);

        }
        // Verifica se existe valor de destino
        if (toData != null) {
            // Taxa de destino
            BigDecimal toTaxa = new BigDecimal(formatNumberDecimal(toData[TAXA_COMPRA]));
            // Montante digitado para conversão
            BigDecimal nMontante = new BigDecimal(montante.floatValue());
            value = nMontante.divide(toTaxa, 10, RoundingMode.DOWN);
        }
        return value.setScale(2, RoundingMode.DOWN);
    }

    /**
     * Retorna verdadeiro se a moeda for do tipo A
     *
     * @param data Array com dados da moeda selecionada
     * @return boolean
     */
    private boolean isCoinTypeA(String[] data) {
        return data[TIPO_MOEDA].equals("A");
    }

    /**
     * Retorna o numero formatado em padrão americano
     *
     * @param s Objeto com valor
     * @return String
     */
    private String formatNumberDecimal(String s) {
        return s.replace(".", "").replace(',', '.');
    }

    /**
     * Retorna Map com dados das moedas carregados em memória
     *
     * @param quotation Data de conversão desejada
     * @return {@code Map<String, String[]>}
     * @throws Exception Problema na rotina e busca dos dados da moeda
     */
    private Map<String, String[]> getCsvData(String quotation) throws Exception {
        try {
            // Executa download do arquivo
            CsvFileDownload csvFileDownload = new CsvFileDownload();
            File f = csvFileDownload.getCsvFile(quotation);
            // Executa leitura do arquivo csv
            CsvReader reader = new CsvReader();
            return reader.getCsv(f);
        } catch (Exception e) {
            throw e;
        }
    }
}
