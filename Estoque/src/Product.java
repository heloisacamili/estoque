import java.util.Date;

public class Product {
	private String produto;
	private String cor;
	private String tamanho;
	private String deposito;
	private Date dataDisponibilidade;
	private int quantidade;
	
	public Product(String produto, String cor, String tamanho, 
			String deposito, Date dataDisponibilidade, int quantidade) {  
		this.produto = produto;
		this.cor = cor;
		this.tamanho = tamanho;
		this.deposito = deposito;
		this.dataDisponibilidade = dataDisponibilidade;
		this.quantidade = quantidade;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getDeposito() {
		return deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public Date getDataDisponibilidade() {
		return dataDisponibilidade;
	}

	public void setDataDisponibilidade(Date dataDisponibilidade) {
		this.dataDisponibilidade = dataDisponibilidade;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
}
