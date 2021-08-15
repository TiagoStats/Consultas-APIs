package CodigosTeste;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

class Dados_Retorno {

    private final String url;
    private final String total;
    private final String status;
    private final String mensagem;
    private final String api_limite;
    private final String api_consultas;
    private final List<Item> item;

    public Dados_Retorno(String url, String total, String status, String mensagem, String api_limite, String api_consultas, List<Item> item) {
        this.url = url;
        this.total = total;
        this.status = status;
        this.mensagem = mensagem;
        this.api_limite = api_limite;
        this.api_consultas = api_consultas;
        this.item = item;
    }
    
    public String getNumeroDeConsultas(){
    	return api_consultas;
    }
    
    public String getSituacaoCadastral() {
    	
    	String[] itens = item.stream().map(String::valueOf).toArray(String[]::new);
    	
    	if (itens == null) {
    		return "Médico não encontrado!";
    	} else {
    		return itens[0];
    	}
    	
    }
    
}


class Item {

    private final String tipo;
    private final String nome;
    private final String numero;
    private final String profissao;
    private final String uf;
    private final String situacao;

    public Item(String tipo, String nome, String numero, String profissao, String uf, String situacao) {
        this.tipo = tipo;
        this.nome = nome;
        this.numero = numero;
        this.profissao = profissao;
        this.uf = uf;
        this.situacao = situacao;
    }
        
    @Override
    public String toString() {
    	//Retorna somente a situação cadastral que é o que queremos neste caso. Entretanto, todos os atributos da classe Item poderiam vir nesse retorno caso fosse necessário.
        return situacao;
    }
}

public class API_Return_Treatment {

		public static void main(String[] args) {
			
			Gson gson = new Gson();
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("TIPO: ");
			String tipo = sc.next();
			
			System.out.print("UF: ");
			String uf = sc.next();
			
			System.out.print("CADASTRO: ");
			String nome = sc.next();

			try {
				//Abaixo, além de inserir os Inputs do usuário na URL de chamada da API, realizamos a conexão;
				URL url = new URL("https://www.consultacrm.com.br/api/index.php?tipo="+tipo+"&uf="+uf+"&q="+nome+"&chave=1185342101&destino=json");
				HttpURLConnection conector = (HttpURLConnection) url.openConnection();
				conector.setDoOutput(true);
				conector.setRequestMethod("GET");
				
				if (conector.getResponseCode() != 200) { //Tratando um possível erro de conexão;
					System.out.print("ERROR... HTTP error code : " + conector.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conector.getInputStream())));

				String output, retorno=""; 
				 
				while ((output = br.readLine()) != null) {
					retorno+=output; // Neste While é adicionado todo o retorno da API dentro da variável 'retorno'
				}
				
				
				Dados_Retorno dados_retorno = gson.fromJson(retorno, Dados_Retorno.class);//Pega o JSON que veio da API e coloca dentro da Classe que criei no começo;
				
				System.out.println(dados_retorno.getSituacaoCadastral());

				conector.disconnect();

			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			
		}

}
