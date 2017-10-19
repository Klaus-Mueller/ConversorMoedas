/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversor.moedas;

import conversor.janela.FrameConversor;
import javax.swing.JFrame;

/**
 * Executa conversão de moedas a partir do site:
 * http://www4.bcb.gov.br/pec/taxas/batch/cotacaomoedas.asp?id=txtodas
 *
 * @author klaus
 */
public class ConversorMoedas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FrameConversor fc = new FrameConversor();
        fc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fc.setTitle("Conversor de moedas");
        fc.setVisible(true);
    }

}
