/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversor.moedas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pela leitura do arquivo csv
 *
 * @author klaus
 */
public class CsvReader {

    /**
     * Método responsável pela leitura e retorno dos dados contidos no csv
     *
     * @param file Objeto com dados do arquivo csv
     * @return {@code Map<String, String[]>}
     * @throws Exception Problema na rotina de leitura do arquivo
     */
    public Map<String, String[]> getCsv(File file) throws Exception {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        Map<String, String[]> data = new HashMap();
        try {
            // Lê arquivo csv e guarda os dados em memória
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                data.put(values[3], values);
            }
        } catch (Exception ex) {
            throw new Exception("Erro na leitura do arquivo csv! " + ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new Exception("Erro no fechamento do arquivo csv! " + e);
                }
            }
        }
        return data;
    }
}
