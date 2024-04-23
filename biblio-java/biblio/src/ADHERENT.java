import java.util.ArrayList;

public class ADHERENT {
	private String num;
	private String nom;
	private String prenom;
	private String email;
	private ArrayList<LIVRE> ListLivre;
	
	public void ajouterLivre(LIVRE l)
	{
	
			
		ListLivre.add(l);
	}
	
	public ADHERENT(String num, String nom, String prenom, String email) {
		super();
		this.setNum(num);
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		ListLivre=new ArrayList<LIVRE>();
	}
	public ADHERENT(String nom, String prenom, String email) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		ListLivre = new ArrayList<LIVRE>();
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<LIVRE> getListLivre() {
		return ListLivre;
	}
	public void setListLivre(ArrayList<LIVRE> listLivre) {
		ListLivre = listLivre;
	}
	
	public void AfficherMesLivres()
	{
		for(int i=0;i!=ListLivre.size();i++)
		{
			ListLivre.get(i).AFFICHER();
		}
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	

	

}
