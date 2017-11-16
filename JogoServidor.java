/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author 13104395
 */
@WebService(serviceName = "JogoServidor")
public class JogoServidor {

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "preRegistro")
    public int preRegistro() {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "registraJogador")
    public int registraJogador(@WebParam(name = "nome") String nome) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "encerraPartida")
    public int encerraPartida(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "temPartida")
    public int temPartida(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "obtemOponente")
    public String obtemOponente(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "ehMinhaVez")
    public int ehMinhaVez(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "obtemTabuleiro")
    public String obtemTabuleiro(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "posicionaPeca")
    public int posicionaPeca(@WebParam(name = "id") int id, @WebParam(name = "pos") int pos, @WebParam(name = "orientacao") int orientacao) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "movePeca")
    public int movePeca(@WebParam(name = "id") int id, @WebParam(name = "posAtual") int posAtual, @WebParam(name = "sentidoDesl") int sentidoDesl, @WebParam(name = "casasDeslocadas") int casasDeslocadas, @WebParam(name = "orientacao") int orientacao) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "verificaLinhas")
    public int verificaLinhas(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "verificaColunas")
    public int verificaColunas(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "verificaDiagonais")
    public int verificaDiagonais(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "venceu")
    public int venceu(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tabuleiroCheio")
    public boolean tabuleiroCheio(@WebParam(name = "id") int id) {
        //TODO write your implementation code here:
        return false;
    }
}
