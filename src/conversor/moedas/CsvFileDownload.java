/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversor.moedas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author klaus
 */
public class CsvFileDownload {

    /* Url responsável para download dos arquivos csv */
    private final String URL_DOWNLOAD = "http://www4.bcb.gov.br/Download/fechamento/";
    /* Caminho para salvar o arquivo */
    private final String FILE_PATH = "csv";
    /* Objeto respnsável pelas datas */
    private static final Calendar c = Calendar.getInstance();

    /**
     * Retorna o arquivo csv
     *
     * @param date Data da cotação desejada na conversão
     * @return String
     * @throws Exception Erro na busca do arquivo csv
     */
    public File getCsvFile(String date) throws Exception {
        Date dateFile = convertToDate(date);
        // Verifica se o dia digitado é um dia válido
        if (isNonWorkingDay(dateFile)) {
            dateFile = getWorkingDay(dateFile);
        }
        // Cria nome do arquivo de download
        String dateFileName = new SimpleDateFormat("yyyyMMdd").format(dateFile);
        String fileName = dateFileName.concat(".csv");
        // Verifica existência do arquivo
        File file = new File(FILE_PATH + File.separator + fileName);
        if (file.exists()) {
            return file;
        } else {
            // Faz download do arquivo CSV
            downloadCsvFile(dateFileName);
        }
        return new File(FILE_PATH + File.separator + fileName);
    }

    /**
     * Classe responsável por retornar a data de download do arquivo de cotação
     *
     * @param date Data da cotação desejada na conversão
     * @return Date
     * @throws ParseException Problema na rotina de converter string para Date
     */
    private Date convertToDate(String date) throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date nDate;
        try {
            nDate = f.parse(date);
        } catch (ParseException ex) {
            throw new Exception("Data digitada está inválida, favor utilizar o padrão dd/MM/yyyy");
        }
        return nDate;
    }

    /**
     * Verifica se o dia digitado é um dia de trabalho válido
     *
     * @param date Data da cotação desejada na conversão
     * @return
     */
    private boolean isNonWorkingDay(Date date) {
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * Retorna dia de trabalho válido
     *
     * @param date Data da cotação desejada na conversão
     * @return Date
     */
    private Date getWorkingDay(Date date) {
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -1);
        if (isNonWorkingDay(c.getTime())) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        return c.getTime();
    }

    /**
     * Executa o download e salva o arquivo CSV
     *
     * @param dateFile nome do arquivo csv a ser baixado
     *
     * @throws Exception Problema na rotina de conexão e download
     */
    private void downloadCsvFile(String dateFile) throws Exception {
        int responseCode = 0;
        try {
            // Cria nome do arquivo de download
            String fileName = dateFile.concat(".csv");
            String link = URL_DOWNLOAD.concat(fileName);
            URL url = new URL(link);
            // Executa conexão com a URL 
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            responseCode = connection.getResponseCode();
            // Se encontrou dados da requisição
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                // Caminho para salvar arquivo
                String saveFilePath = FILE_PATH + File.separator + fileName;
                // Verifica se já existe pasta para salvar o arquivo
                File file = new File(FILE_PATH);
                if (!file.exists()) {
                    file.mkdir();
                }
                // Inicia o salvamento dos dados do arquivo
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
            } else {
                throw new Exception("Não existe arquivo para download. HTTP code:" + responseCode);
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
