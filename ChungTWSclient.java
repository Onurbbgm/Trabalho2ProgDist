/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chungtwsclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author roland
 */
public class ChungTWSclient {

    static chungtwsclient.JogoServidor port;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        chungtwsclient.JogoServidor_Service service = new chungtwsclient.JogoServidor_Service();
        port = service.getJogoServidorPort();
        executaTeste("ChungToi-0000");
//        executaTeste("ChungToi-0100");
//        executaTeste("ChungToi-2000");
//        executaTeste("ChungToi-2500");
//        executaTeste("ChungToi-3000");
//        executaTeste("ChungToi-4000");
//        executaTeste("ChungToi-4500");
//			System.out.println("Comecou");
			Scanner entrada = new Scanner(System.in);
//			Scanner entrada2 = new Scanner(System.in);
//			Scanner posE = new Scanner(System.in);
//			Scanner oriE = new Scanner(System.in);
//			Scanner posMovE = new Scanner(System.in);
//			Scanner senDesE = new Scanner(System.in);
//			Scanner numCasDesE = new Scanner(System.in);
//			Scanner oriMovE = new Scanner(System.in);
			String nome = "";
			System.out.println("Informe seu nome: ");
			nome = entrada.next();
			int id = port.registraJogador(nome);
			System.out.println("Seu id:"+id);
			int continua = 1;
			int opcao = 0;
			while(continua!=0){
				System.out.println("O que voce gostaria de fazer(escolha um numero): ");
				System.out.println("1 - Obter oponente");
				System.out.println("2 - Tem partida");
				System.out.println("3 - Obter tabuleiro");
				System.out.println("4 - Eh minha vez/Verifica vencedor");
				System.out.println("5 - Posiciona peca");
				System.out.println("6 - Move peca");
				System.out.println("7 - Encerrar partida");
				opcao = entrada.nextInt();
				switch(opcao){
					case 1: String o = port.obtemOponente(id);
						System.out.println(o);
						if(o.equals("Tempo Esgotado")){
							System.out.println(port.encerraPartida(id));
							continua = 0;
						}
						break;
					case 2: System.out.println(port.temPartida(id));
						break;
					case 3: System.out.println(port.obtemTabuleiro(id));
						break;
					case 4:System.out.println(port.ehMinhaVez(id)); 
						break;
					case 5: System.out.println("Qual a posicao desejada (0a8): ");
							int pos = entrada.nextInt();
							System.out.println("Qual a orientacao da peca (0ou1): ");
							int ori = entrada.nextInt();
							System.out.println(port.posicionaPeca(id, pos, ori));
						break;
					case 6: System.out.println("Qual pos voce quer mover (0a8): ");
							int posMov = entrada.nextInt();
							System.out.println("Sentido que voce quer mover (0a8): ");
							int senDes = entrada.nextInt();
							System.out.println("Numero de casas deslocadas (0,1ou2): ");
							int numDes = entrada.nextInt();
							System.out.println("Orientacao da peca (0ou1):");
							int oriMov = entrada.nextInt();
							System.out.println(port.movePeca(id, posMov, senDes, numDes, oriMov));
						break;
					case 7:System.out.println(port.encerraPartida(id)); 
						continua = 0;
						break;
					default: System.out.println("Opcao invalida!");
							break;
				}
			}
			System.out.println("Fim!");
        
    }


    
    private static void executaTeste(String rad) throws IOException {
        String inFile = rad+".in";
        FileInputStream is = new FileInputStream(new File(inFile));
        System.setIn(is);

        String outFile = rad+".out";
        FileWriter outWriter = new FileWriter(outFile);
        try (PrintWriter out = new PrintWriter(outWriter)) {
            Scanner leitura = new Scanner(System.in);
            int numOp = leitura.nextInt();
            for (int i=0;i<numOp;++i) {
                System.out.print("\r"+rad+": "+(i+1)+"/"+numOp);
                int op = leitura.nextInt();
                String parametros = leitura.next();
                String param[] = parametros.split(":",-1);
                switch(op) {
                    case 0:
                        if (param.length!=4)
                            erro(inFile,i+1);
                        else
                            out.println(port.preRegistro(param[0],Integer.parseInt(param[1]),param[2],Integer.parseInt(param[3])));
                        break;
                    case 1:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.registraJogador(param[0]));
                        break;
                    case 2:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.encerraPartida(Integer.parseInt(param[0])));
                        break;
                    case 3:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.temPartida(Integer.parseInt(param[0])));
                        break;
                    case 4:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemOponente(Integer.parseInt(param[0])));
                        break;
                    case 5:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.ehMinhaVez(Integer.parseInt(param[0])));
                        break;
                    case 6:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemTabuleiro(Integer.parseInt(param[0])));
                        break;
                    case 7:
                        if (param.length!=3)
                            erro(inFile,i+1);
                        else
                            out.println(port.posicionaPeca(Integer.parseInt(param[0]),Integer.parseInt(param[1]),Integer.parseInt(param[2])));
                        break;
                    case 8:
                        if (param.length!=5)
                            erro(inFile,i+1);
                        else
                            out.println(port.movePeca(Integer.parseInt(param[0]),Integer.parseInt(param[1]),Integer.parseInt(param[2]),Integer.parseInt(param[3]),Integer.parseInt(param[4])));
                        break;
                    default:
                        erro(inFile,i+1);
                }
            }
            System.out.println("... terminado!");
            out.close();
            leitura.close();
        }
    }
    
    private static void erro(String arq,int operacao) {
        System.err.println("Entrada invalida: erro na operacao "+operacao+" do arquivo "+arq);
        System.exit(1);
    }
    
}