/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogoServ;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Bruno
 */

class Jogador{
	public String nome;
	public int id;
	public int numJogador;
	public int vez;
	public int estaNumPartida;
	public Jogador(String nome, int id, int numJogador, int vez, int estaNumPartida){
		this.nome = nome;
		this.id = id;
		this.numJogador = numJogador;
		this.vez = vez;
		this.estaNumPartida = estaNumPartida;
	}
}

class Jogo{
//    public ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
//    public ArrayList<Jogo> jogos = new ArrayList<Jogo>();
    public char[][] tabuleiro = new char[3][3];
    public char peca;
    public int idJogador;
    public int idJogo;
    public int numParticipantes;
    public Jogador j;
    public Jogador j2;
   // public long contTemBuscaJog;
    public Jogo(Jogador j1, Jogador j2, char[][] tabuleiro)  {
		this.numParticipantes = numParticipantes;
                this.idJogo = idJogo;
		j = j1;
		this.j2 = j2;
		this.tabuleiro = tabuleiro;
		for(int linha=0 ; linha<3 ; linha++){
			for(int coluna=0 ; coluna<3 ; coluna++){
				tabuleiro[linha][coluna]='.';
			}      
		}
	}  
    
}

@WebService(serviceName = "JogoServidor")
public class JogoServidor {

    private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
    private ArrayList<Jogo> jogos = new ArrayList<Jogo>();
    private ArrayList<Jogador> preRegistrados = new ArrayList<Jogador>();
    private ArrayList<Jogo> preJogos = new ArrayList<Jogo>();
   // private char[][] tabuleiro = new char[3][3];
    //private char peca;
    private static int idJogador = 1;
    private static int idJogo = 1;
    //private int numParticipantes;
//    private Jogador j;
//    private Jogador j2;
   // private long contTemBuscaJog;
    

    
    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "preRegistro")
    public int preRegistro(String j1, int id1, String j2, int id2) {
        Jogador pre1 = new Jogador(j1, id1, 1, 1, 1);
        preRegistrados.add(pre1);
        System.out.println(pre1.id+" "+pre1.nome);
        Jogador pre2 = new Jogador(j2, id2, 2, 0, 1);
        System.out.println(pre2.id +" "+pre2.nome);
        preRegistrados.add(pre2);
        char[][] tab = new char[3][3];
	for(int linha=0 ; linha<3 ; linha++){
            for(int coluna=0 ; coluna<3 ; coluna++){
		tab[linha][coluna]='.';
            }      
	}
        Jogo preJogo = new Jogo(pre1,pre2,tab);
        preJogo.numParticipantes = 2;
        preJogo.idJogo = idJogo*5;
        preJogos.add(preJogo);
        System.out.println(preJogo.j.nome +" "+preJogo.j2.nome);
        return 0;
    }
    
    

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "registraJogador")
    public int registraJogador(@WebParam(name = "nome") String nome) {
       int id = idJogador;
       if(!preRegistrados.isEmpty()){
        for(int h = 0; h<preRegistrados.size(); h++){
               if(preRegistrados.get(h).nome.equals(nome)){
                   return preRegistrados.get(h).id;
                }
        }
       }
		if(jogadores.size()>=1001){
			return -2;//numero maximo de jogadores atingidos
		}
		for(int i = 0; i<jogadores.size(); i++){
			if(nome.equals(jogadores.get(i).nome)){
				return -1; //Usuariao ja cadastrado
			}
		}
                int p = 0;
                while(p<preRegistrados.size()){
                    if(preRegistrados.get(p).id == id){
                        id=id+1;
                    }
                    p++;
                }
		Jogador j = new Jogador(nome,id,0,0,0);
		jogadores.add(j);
		idJogador++;
		//contTemBuscaJog = System.currentTimeMillis();
		return id;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "encerraPartida")
    public int encerraPartida(@WebParam(name = "id") int id) {
    
        for(int b = 0; b<preJogos.size(); b++){
            if(preJogos.get(b).j.id == id || preJogos.get(b).j2.id == id){
                for(int c = 0; c<preRegistrados.size(); c++){
                        if(preRegistrados.get(c).id == id){
                            preRegistrados.remove(c);
                            preJogos.remove(b);
                            return 0;
                        }
                    }
            }
        }
        
        for(int i = 0; i<jogos.size(); i++){
            if((jogos.get(i).j.id == id  || jogos.get(i).j2.id == id) && jogos.get(i).numParticipantes == 2){
               // int id2 = jogos.get(i).j2.id;
                for(int p = 0; p<preRegistrados.size(); p++){
                    if(preRegistrados.get(p).id == id){
                        preRegistrados.remove(p);
                        jogos.remove(i);
                        return 0;
                    }
                }
                for(int j = 0; j<jogadores.size(); j++){
                    if(jogadores.get(j).id == id){
                        jogadores.remove(j);                                            
                    }
                }
                    jogos.remove(i);
                    return 0;//partida encerradad com sucesso
            }
//            if(jogos.get(i).j2.id == id && jogos.get(i).numParticipantes == 2){
//              //  int id2 = jogos.get(i).j.id;
//                for(int l = 0; l<jogadores.size(); l++){
//                    if(jogadores.get(l).id == id){
//                        jogadores.remove(l);                                            
//                    }
//                }
//                    jogos.remove(i);
//                    return 0;//partida encerradad com sucesso
//            }
            else{
                for(int a = 0; a<preRegistrados.size(); a++){
                    if(preRegistrados.get(a).id == id){
                        preRegistrados.remove(a);
                        return 0;
                    }
                }
                for(int h = 0; h<jogadores.size(); h++){
                    if(jogadores.get(h).id == id){
                        jogadores.remove(h);
                        return 0;
                    }
                }
            }
	}		

	
	return -1;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "temPartida")
    public int temPartida(@WebParam(name = "id") int id) {
        for(int i = 0; i<jogos.size(); i++){
            if(jogos.get(i).j.id == id && jogos.get(i).numParticipantes == 1){
		return 0;//ainda nao ha partida
            }
            if(jogos.get(i).j.id == id && jogos.get(i).numParticipantes == 2){
            	if(jogos.get(i).j.numJogador!=0){
                    return jogos.get(i).j.numJogador;
		}				
		if(jogos.get(i).j2.numJogador==1){
                    jogos.get(i).j.numJogador = 2;
                    jogos.get(i).j.vez = 0;
                    return jogos.get(i).j.numJogador;
		}
		if(jogos.get(i).j2.numJogador==2){
                    jogos.get(i).j.numJogador = 1;
                    jogos.get(i).j.vez = 1;
                    return jogos.get(i).j.numJogador;
		}else{
                    Random rand = new Random();
		    int randomNum = rand.nextInt((2 - 1) + 1) + 1;
		    jogos.get(i).j.numJogador = randomNum;
		    if(randomNum==1){
                    jogos.get(i).j.vez = 1;
                    jogos.get(i).j2.numJogador = 2;
                    jogos.get(i).j2.vez = 0;
		}
		    if(randomNum == 2){
		   	jogos.get(i).j2.vez = 1;
		    	jogos.get(i).j2.numJogador = 1;
		   	jogos.get(i).j.vez = 0;
		    }
//                    System.out.println("NumJogador j1: " + jogos.get(i).j.numJogador);
//		    System.out.println("NumJogador j2: " + jogos.get(i).j2.numJogador);
                    return randomNum;
		}		
            }
        }
        return -1;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "obtemOponente")
    public String obtemOponente(@WebParam(name = "id") int id) {
        int idJog = idJogo;
        String nome = "";
        for(int p = 0; p<preJogos.size(); p++){
            for(int a = 0; a<jogos.size(); a++){
                if(jogos.get(a).j.id == id || jogos.get(a).j2.id == id){
                    return "";
                }
            }
            if(preJogos.get(p).j.id == id || preJogos.get(p).j2.id == id){
                jogos.add(preJogos.get(p));             
                if(preJogos.get(p).j.id == id){
                    nome = preJogos.get(p).j.nome;
                    preJogos.remove(p);
                    return nome;
                }
                else{
                    nome = preJogos.get(p).j2.nome;
                    preJogos.remove(p);
                    return nome;
                }
            }
        }
        //long tempoPassado = System.currentTimeMillis() - contTemBuscaJog;
     //   long segundos = tempoPassado /1000;
//	System.out.println(segundos);
//	if(segundos>=120){
//            return "Tempo Esgotado";	
//	}
        String n = "";
	char[][] tab = new char[3][3];
	for(int linha=0 ; linha<3 ; linha++){
            for(int coluna=0 ; coluna<3 ; coluna++){
		tab[linha][coluna]='.';
            }      
	}
	Jogador j1 = null;
	for(int h = 0; h<jogadores.size(); h++){
            if(jogadores.get(h).id == id){
		j1 = jogadores.get(h);
            }
	}	
	if(j1.id == id && j1.estaNumPartida == 0){
            for(int l = 0; l<jogadores.size(); l++){
		if(jogadores.get(l).id != id && jogadores.get(l).estaNumPartida == 0 && jogos.size() < 500){
                 //   System.out.println("Id adversario: "+jogadores.get(l).id);
                    Jogador j2 = jogadores.get(l);
                    n = j2.nome;
                    j2.estaNumPartida = 1;
                    j1.estaNumPartida = 1;
                    Jogo jog = new Jogo(j1,j2,tab);
                    jog.numParticipantes = 2;
                    jog.idJogo = idJog;
                    jogos.add(jog);
                    idJogo++;
                    return "Seu oponente: "+n;
		}
            }		
        }	
	return "";//erro
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "ehMinhaVez")
    public int ehMinhaVez(@WebParam(name = "id") int id) {
    
		for(int i = 0; i<jogos.size(); i++){
			if((jogos.get(i).j.id==id && jogos.get(i).numParticipantes !=2) || (jogos.get(i).j2.id==id && jogos.get(i).numParticipantes !=2)){
				return -2; //ainda nao tem 2 jogadores
			}
			if((jogos.get(i).j.id==id && venceu(id)!=0 && venceu(id)==jogos.get(i).j.numJogador)||(jogos.get(i).j2.id==id && venceu(id)!=0 && venceu(id)==jogos.get(i).j2.numJogador)){
				return 2; //eh o vencedor
			}
			if((jogos.get(i).j.id==id && venceu(id)!=0 && venceu(id)!=jogos.get(i).j.numJogador) || (jogos.get(i).j2.id==id && venceu(id)!=0 && venceu(id)!=jogos.get(i).j2.numJogador)){
				return 3; //eh o perdedor
			}
//			if(jogos.get(i).j.id == id && tempo1 == 0){ //placeholder
//				return 6; //perdedor por WO
//			}
//			if(jogos.get(i).j.id == id && tempo2 == 0){ //placeholder
//				return 5;//vencedor por WO
//			}
			if((jogos.get(i).j.id == id && jogos.get(i).j.vez == 1) || (jogos.get(i).j2.id == id && jogos.get(i).j2.vez == 1)){
				return 1;//sim
			}
			if((jogos.get(i).j.id == id && jogos.get(i).j.vez == 0) || (jogos.get(i).j2.id == id && jogos.get(i).j2.vez == 0)){
				return 0;//nao
			}
			
		}
		return -1;//erro
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "obtemTabuleiro")
    public String obtemTabuleiro(@WebParam(name = "id") int id) {
        String tab = "";
        for(int i = 0; i<jogos.size(); i++){
			if(jogos.get(i).j.id==id || jogos.get(i).j2.id==id ){	
		        for(int linha=0 ; linha<3 ; linha++){
		        
		            for(int coluna=0 ; coluna<3 ; coluna++){
		                
		                if(jogos.get(i).tabuleiro[linha][coluna]== '.'){
		                    tab += ".";
		                }
		                if(jogos.get(i).tabuleiro[linha][coluna]=='c'){
		                    tab += "c";
		                }
		                if(jogos.get(i).tabuleiro[linha][coluna]=='C'){
		                    tab += "C";
		                }
		                if(jogos.get(i).tabuleiro[linha][coluna]=='e'){
		                    tab += "e";
		                }
		                if(jogos.get(i).tabuleiro[linha][coluna]=='E'){
		                    tab += "E";
		                }
		                
//		                if(coluna==0 || coluna==1)
//		                    tab += "|";
		            }
//		            tab += "\n";
		        }
				return tab;
			}
		}
		
			return "";//erro
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "posicionaPeca")
    public int posicionaPeca(@WebParam(name = "id") int id, @WebParam(name = "pos") int pos, @WebParam(name = "orientacao") int orientacao) {
       int vez = ehMinhaVez(id);
		if(vez == 1 && !tabuleiroCheio(id)){
			for(int i = 0; i<jogos.size(); i++){
				if((jogos.get(i).j.id == id || jogos.get(i).j2.id == id) && jogos.get(i).numParticipantes!=2){
					return -2;//ainda nao tem dois jogadores na partida
				}
				if((jogos.get(i).j.id == id || jogos.get(i).j2.id == id) && jogos.get(i).numParticipantes==2){
					//caso seja o jogador 1(claras)
					if((jogos.get(i).j.numJogador == 1 && jogos.get(i).j.vez ==1) || (jogos.get(i).j2.numJogador == 1 && jogos.get(i).j2.vez == 1)){
						if(pos == 0 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[0][0] == '.'){
								jogos.get(i).tabuleiro[0][0] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
//								for(int l = 0; l<jogos.size(); l++){
//									if(jogos.get(l).idJogo == jogos.get(i).idJogo && jogos.get(l).j.id != id){
//										jogos.get(l).j.vez = 1;
//									}
//								}
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 0 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[0][0] == '.'){
								jogos.get(i).tabuleiro[0][0] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
//								for(int l = 0; l<jogos.size(); l++){
//									if(jogos.get(l).idJogo == jogos.get(i).idJogo && jogos.get(l).j.id != id){
//										jogos.get(l).j.vez = 1;
//									}
//								}
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 1 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[0][1] == '.'){
								jogos.get(i).tabuleiro[0][1] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
//								for(int l = 0; l<jogos.size(); l++){
//									if(jogos.get(l).idJogo == jogos.get(i).idJogo && jogos.get(l).j.id != id){
//										jogos.get(l).j.vez = 1;
//									}
//								}
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 1 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[0][1] == '.'){
								jogos.get(i).tabuleiro[0][1] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;							
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 2 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[0][2] == '.'){
								jogos.get(i).tabuleiro[0][2] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 2 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[0][2] == '.'){
								jogos.get(i).tabuleiro[0][2] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 3 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[1][0] == '.'){
								jogos.get(i).tabuleiro[1][0] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 3 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[1][0] == '.'){
								jogos.get(i).tabuleiro[1][0] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 4 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[1][1] == '.'){
								jogos.get(i).tabuleiro[1][1] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 4 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[1][1] == '.'){
								jogos.get(i).tabuleiro[1][1] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 5 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[1][2] == '.'){
								jogos.get(i).tabuleiro[1][2] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 5 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[1][2] == '.'){
								jogos.get(i).tabuleiro[1][2] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 6 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[2][0] == '.'){
								jogos.get(i).tabuleiro[2][0] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 6 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[2][0] == '.'){
								jogos.get(i).tabuleiro[2][0] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 7 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[2][1] == '.'){
								jogos.get(i).tabuleiro[2][1] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 7 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[2][1] == '.'){
								jogos.get(i).tabuleiro[2][1] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 8 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh C
							if(jogos.get(i).tabuleiro[2][2] == '.'){
								jogos.get(i).tabuleiro[2][2] = 'C';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 8 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh c
							if(jogos.get(i).tabuleiro[2][2] == '.'){
								jogos.get(i).tabuleiro[2][2] = 'c';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						
					}
					//caso seja o jogador 2(escuras)
					if((jogos.get(i).j.numJogador == 2 && jogos.get(i).j.vez ==1) || (jogos.get(i).j2.numJogador == 2 && jogos.get(i).j2.vez == 1)){
						if(pos == 0 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[0][0] == '.'){
								jogos.get(i).tabuleiro[0][0] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 0 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[0][0] == '.'){
								jogos.get(i).tabuleiro[0][0] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 1 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[0][1] == '.'){
								jogos.get(i).tabuleiro[0][1] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 1 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[0][1] == '.'){
								jogos.get(i).tabuleiro[0][1] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 2 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[0][2] == '.'){
								jogos.get(i).tabuleiro[0][2] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 2 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[0][2] == '.'){
								jogos.get(i).tabuleiro[0][2] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 3 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[1][0] == '.'){
								jogos.get(i).tabuleiro[1][0] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 3 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[1][0] == '.'){
								jogos.get(i).tabuleiro[1][0] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 4 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[1][1] == '.'){
								jogos.get(i).tabuleiro[1][1] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 4 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[1][1] == '.'){
								jogos.get(i).tabuleiro[1][1] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 5 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[1][2] == '.'){
								jogos.get(i).tabuleiro[1][2] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 5 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[1][2] == '.'){
								jogos.get(i).tabuleiro[1][2] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 6 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[2][0] == '.'){
								jogos.get(i).tabuleiro[2][0] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 6 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[2][0] == '.'){
								jogos.get(i).tabuleiro[2][0] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 7 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[2][1] == '.'){
								jogos.get(i).tabuleiro[2][1] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 7 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[2][1] == '.'){
								jogos.get(i).tabuleiro[2][1] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 8 && orientacao == 0){ //orientacao = 0 eh perpendicular, que eh E
							if(jogos.get(i).tabuleiro[2][2] == '.'){
								jogos.get(i).tabuleiro[2][2] = 'E';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						if(pos == 8 && orientacao == 1){ //orientacao = 1 eh diagonal, que eh e
							if(jogos.get(i).tabuleiro[2][2] == '.'){
								jogos.get(i).tabuleiro[2][2] = 'e';
								if(jogos.get(i).j2.vez == 0){
									jogos.get(i).j.vez = 0;
									jogos.get(i).j2.vez = 1;
									return 1;
								}
								jogos.get(i).j2.vez = 0;
								jogos.get(i).j.vez = 1;
								return 1;
							}else{
								return -1;
							}
						}
						
					}
					
				}
			}
		}
		if(vez==0){
			return -3;//nao eh a sua vez
		}
		if(tabuleiroCheio(id)){
			return -4;//nao tem mais posicoes livres disponiveis
		}
		
		return -1;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "movePeca")
    public int movePeca(@WebParam(name = "id") int id, @WebParam(name = "posAtual") int posAtual, @WebParam(name = "sentidoDesl") int sentidoDesl, @WebParam(name = "casasDeslocadas") int casasDeslocadas, @WebParam(name = "orientacao") int orientacao) {
        int vez = ehMinhaVez(id);
		if(vez != 1){
			return -3;//nao eh a vez do jogador
		}
		if(!tabuleiroCheio(id)){
			return -4; //tabuleiro nao esta cheio
		}
		
		for(int i = 0; i<jogos.size(); i++){
			if((jogos.get(i).j.id == id || jogos.get(i).j2.id == id) && jogos.get(i).numParticipantes !=2){
				return -2;//partida nao inicada ainda, nao ha dois jogadores
			}
			if((jogos.get(i).j.id == id || jogos.get(i).j2.id == id) && jogos.get(i).numParticipantes == 2){
				//claras
				if((jogos.get(i).j.numJogador == 1 && jogos.get(i).j.vez == 1) || (jogos.get(i).j2.numJogador == 1 && jogos.get(i).j2.vez == 1)){
					//posAtual 0 movimento perpendicular
					if(posAtual == 0 && jogos.get(i).tabuleiro[0][0] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[0][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 0 movimento diagonal
					if(posAtual == 0 && jogos.get(i).tabuleiro[0][0] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 8  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 8  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 1 movimento perpendicular
					if(posAtual == 1 && jogos.get(i).tabuleiro[0][1] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3   && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'c';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 1 movimento diagonal
					if(posAtual == 1 && jogos.get(i).tabuleiro[0][1] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 6   && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 2 movimento perpendicular
					if(posAtual == 2 && jogos.get(i).tabuleiro[0][2] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 2 movimento diagonal
					if(posAtual == 2 && jogos.get(i).tabuleiro[0][2] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 3 movimento perpendicular
					if(posAtual == 3 && jogos.get(i).tabuleiro[1][0] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 3 movimento diagonal
					if(posAtual == 3 && jogos.get(i).tabuleiro[1][0] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtul 4 movimento perpendicular
					if(posAtual == 4 && jogos.get(i).tabuleiro[1][1] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					if(posAtual == 4 && jogos.get(i).tabuleiro[1][1] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}						
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 5 movimento perpendicular
					if(posAtual == 5 && jogos.get(i).tabuleiro[1][2] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 5 movimento diagonal
					if(posAtual == 5 && jogos.get(i).tabuleiro[1][2] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 6 movimento perpendicular
					if(posAtual == 6 && jogos.get(i).tabuleiro[2][0] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[2][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual movimento diagonal
					if(posAtual == 6 && jogos.get(i).tabuleiro[2][0] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular							
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 7 movimento perpendicular
					if(posAtual == 7 && jogos.get(i).tabuleiro[2][1] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3   && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'c';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 7 movimento diagonal
					if(posAtual == 7 && jogos.get(i).tabuleiro[0][1] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 8 movimento perpendicular
					if(posAtual == 8 && jogos.get(i).tabuleiro[2][2] == 'C'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[2][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 8 movimento diagonal
					if(posAtual == 8 && jogos.get(i).tabuleiro[2][2] == 'c'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[2][2] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'C';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'c';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
				}
				//ESCURAS
				if((jogos.get(i).j.numJogador == 2 && jogos.get(i).j.vez == 1) || (jogos.get(i).j2.numJogador == 2 && jogos.get(i).j2.vez == 1) ){
					//posAtual 0 movimento perpendicular
					if(posAtual == 0 && jogos.get(i).tabuleiro[0][0] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[0][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 0 movimento diagonal
					if(posAtual == 0 && jogos.get(i).tabuleiro[0][0] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 8  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 8  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 1 movimento perpendicular
					if(posAtual == 1 && jogos.get(i).tabuleiro[0][1] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3   && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'e';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if( sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 1 movimento diagonal
					if(posAtual == 1 && jogos.get(i).tabuleiro[0][1] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 6   && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 2 movimento perpendicular
					if(posAtual == 2 && jogos.get(i).tabuleiro[0][2] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							jogos.get(i).j.vez = 0;
							for(int l = 0; l<jogos.size(); l++){
								if(jogos.get(l).idJogo == jogos.get(i).idJogo && jogos.get(l).j.id != id){
									jogos.get(l).j.vez = 1;
								}
							}
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 2 movimento diagonal
					if(posAtual == 2 && jogos.get(i).tabuleiro[0][2] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[0][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 3 movimento perpendicular
					if(posAtual == 3 && jogos.get(i).tabuleiro[1][0] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 7 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 3 movimento diagonal
					if(posAtual == 3 && jogos.get(i).tabuleiro[1][0] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular							
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtul 4 movimento perpendicular
					if(posAtual == 4 && jogos.get(i).tabuleiro[1][1] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					if(posAtual == 4 && jogos.get(i).tabuleiro[1][1] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 8  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}						
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][1] = '.';
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 5 movimento perpendicular
					if(posAtual == 5 && jogos.get(i).tabuleiro[1][2] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 7  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 5 movimento diagonal
					if(posAtual == 5 && jogos.get(i).tabuleiro[1][2] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 6  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[1][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 6 movimento perpendicular
					if(posAtual == 6 && jogos.get(i).tabuleiro[2][0] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[2][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual movimento diagonal
					if(posAtual == 6 && jogos.get(i).tabuleiro[2][0] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][0] = '.';
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 7 movimento perpendicular
					if(posAtual == 7 && jogos.get(i).tabuleiro[2][1] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 5 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 5  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3   && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[2][0] = 'e';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][1] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[0][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 7 movimento diagonal
					if(posAtual == 7 && jogos.get(i).tabuleiro[0][1] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][0] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2 && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 2 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][1] = '.';
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 8 movimento perpendicular
					if(posAtual == 8 && jogos.get(i).tabuleiro[2][2] == 'E'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 1){
							jogos.get(i).tabuleiro[2][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 3  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[2][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 3 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[2][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[2][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 1 && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][2] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][2] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					//posAtual 8 movimento diagonal
					if(posAtual == 8 && jogos.get(i).tabuleiro[2][2] == 'e'  ){
						if(sentidoDesl == 4 && casasDeslocadas == 0 && orientacao == 0){
							jogos.get(i).tabuleiro[2][2] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1; //tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 0 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 1 && orientacao == 1 && jogos.get(i).tabuleiro[1][1] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[1][1] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 2 && orientacao == 0 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'E';//perpendicular
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}
						if(sentidoDesl == 0  && casasDeslocadas == 2 && orientacao == 1 && jogos.get(i).tabuleiro[0][0] == '.'){
							jogos.get(i).tabuleiro[2][2] = '.';
							jogos.get(i).tabuleiro[0][0] = 'e';//diagonal
							if(jogos.get(i).j2.vez == 0){
								jogos.get(i).j.vez = 0;
								jogos.get(i).j2.vez = 1;
								return 1;
							}
							jogos.get(i).j2.vez = 0;
							jogos.get(i).j.vez = 1;
							return 1;//tudo certo
						}else{
							return 0;
						}	
					}
					
				}
			}
		}
		
		return -1;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "verificaLinhas")
    public int verificaLinhas(@WebParam(name = "id") int id) {
       for(int i = 0; i < jogos.size(); i++){
			if(jogos.get(i).j.id == id || jogos.get(i).j2.id == id){
				 for(int linha=0 ; linha<3 ; linha++){
			            if( (jogos.get(i).tabuleiro[linha][0] == 'c'||jogos.get(i).tabuleiro[linha][0] == 'C') && (jogos.get(i).tabuleiro[linha][1] == 'c' || jogos.get(i).tabuleiro[linha][1] == 'C') &&  (jogos.get(i).tabuleiro[linha][2] == 'c'||jogos.get(i).tabuleiro[linha][2] == 'C')){
			                return 1;//claras
			            }
			            if( (jogos.get(i).tabuleiro[linha][0] == 'e'||jogos.get(i).tabuleiro[linha][0] == 'E') && (jogos.get(i).tabuleiro[linha][1] == 'e' || jogos.get(i).tabuleiro[linha][1] == 'E') &&  (jogos.get(i).tabuleiro[linha][2] == 'E'||jogos.get(i).tabuleiro[linha][2] == 'E')){
			            	return 2;//escuras
			            }       
			        }
			}
		}
       
        
        return 0;//ninguem completou todas as linhas  
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "verificaColunas")
    public int verificaColunas(@WebParam(name = "id") int id) {
        for(int i = 0; i < jogos.size(); i++){
				if(jogos.get(i).j.id == id || jogos.get(i).j2.id == id){
					 for(int coluna=0 ; coluna<3 ; coluna++){
				        	if( (jogos.get(i).tabuleiro[0][coluna] == 'c'||jogos.get(i).tabuleiro[0][coluna] == 'C') && (jogos.get(i).tabuleiro[1][coluna] == 'c' || jogos.get(i).tabuleiro[1][coluna] == 'C') &&  (jogos.get(i).tabuleiro[2][coluna] == 'c'||jogos.get(i).tabuleiro[2][coluna] == 'C')){
				                return 1;//claras
				            }
				            if( (jogos.get(i).tabuleiro[0][coluna] == 'e'||jogos.get(i).tabuleiro[0][coluna] == 'E') && (jogos.get(i).tabuleiro[1][coluna] == 'e' || jogos.get(i).tabuleiro[1][coluna] == 'E') &&  (jogos.get(i).tabuleiro[2][coluna] == 'E'||jogos.get(i).tabuleiro[2][coluna] == 'E')){
				            	return 2;//escuras
				            }       
				        }
				}
		  }
	       
	        return 0;          
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "verificaDiagonais")
    public int verificaDiagonais(@WebParam(name = "id") int id) {
         for(int i = 0; i < jogos.size(); i++){
				if(jogos.get(i).j.id == id || jogos.get(i).j2.id == id){
					if( (jogos.get(i).tabuleiro[0][0] == 'c'||jogos.get(i).tabuleiro[0][0] == 'C') && (jogos.get(i).tabuleiro[1][1] == 'c' || jogos.get(i).tabuleiro[1][1] == 'C') &&  (jogos.get(i).tabuleiro[2][2] == 'c'||jogos.get(i).tabuleiro[2][2] == 'C')){
						 return 1;//claras
					 }
					 if( (jogos.get(i).tabuleiro[0][0] == 'e'||jogos.get(i).tabuleiro[0][0] == 'E') && (jogos.get(i).tabuleiro[1][1] == 'e' || jogos.get(i).tabuleiro[1][1] == 'E') &&  (jogos.get(i).tabuleiro[2][2] == 'e'||jogos.get(i).tabuleiro[2][2] == 'E')){
						 return 2;//escuras
					 }
					 if( (jogos.get(i).tabuleiro[0][2] == 'c'||jogos.get(i).tabuleiro[0][0] == 'C') && (jogos.get(i).tabuleiro[1][1] == 'c' || jogos.get(i).tabuleiro[1][1] == 'C') &&  (jogos.get(i).tabuleiro[2][2] == 'c'||jogos.get(i).tabuleiro[2][0] == 'C')){
						 return 1;//claras
					 }
					 if( (jogos.get(i).tabuleiro[0][2] == 'e'||jogos.get(i).tabuleiro[0][0] == 'E') && (jogos.get(i).tabuleiro[1][1] == 'e' || jogos.get(i).tabuleiro[1][1] == 'E') &&  (jogos.get(i).tabuleiro[2][2] == 'e'||jogos.get(i).tabuleiro[2][0] == 'E')){
						 return 2;//escuras
					 }
				}
		 }
		 
		 return 0;
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "venceu")
    public int venceu(@WebParam(name = "id") int id) {
       if(verificaLinhas(id) == 1){
			 return 1;//claras
		 }
		 if(verificaColunas(id) == 1){
			 return 1;//claras
		 }
		 if(verificaDiagonais(id) == 1){
			 return 1;//claras
		 }
		 if(verificaLinhas(id) == 2){
			 return 2;//escuras
		 }
		 if(verificaColunas(id) == 2){
			 return 2;//escuras
		 }
		 if(verificaDiagonais(id) == 2){
			 return 2;//escuras
		 }
		 return 0;//ninguem venceu ainda
    }

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "tabuleiroCheio")
    public boolean tabuleiroCheio(@WebParam(name = "id") int id) {
       int cont = 0;
		 for(int i = 0; i < jogos.size(); i++){
				if(jogos.get(i).j.id == id || jogos.get(i).j2.id == id){
					for(int linha=0 ; linha<3 ; linha++){
			        	for(int coluna=0 ; coluna<3 ; coluna++){
			        		if( jogos.get(i).tabuleiro[linha][coluna]!='.' ){
			        			 cont++;
			        		}
			        	}
			        } 
				}
		 }
	        
		if(cont>=6){
	       	return true;
	     }
		 return false;
    }
}
