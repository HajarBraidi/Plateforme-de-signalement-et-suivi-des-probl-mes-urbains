package dao;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import metier.Signalement;
import metier.Statut;

public class SignalementCRUDTest {

	private SignalementCRUDImpl signalementDAO;

	@Before
	public void setUp() throws Exception {
		signalementDAO = new SignalementCRUDImpl();
	}
	
	@Test
	public void testCreateSignalement() {
		Signalement s = new Signalement();
		
		s.setDescription("Ampoule ne fonctionne plus");
		s.setLocalisation("somewhere in the world");
		s.setDateCreation(new Date(System.currentTimeMillis()));
		s.setImagePath("image path");		
		s.setStatut(Statut.NEW);
		 s.setDesignation("Éclairage - Test Get");
		s.setCommentaire("je souhaite que le problème se résoud au plutot" );
		s.setIdCitoyen(null);
		
		signalementDAO.createSignalement(s);

        assertNotNull("L'ID du signalement doit être généré",s.getIdSignalement());
	}

	@Test
	public void testDeleteSignalement() {
		
		Signalement s = new Signalement();

		s.setDescription("Ampoule ne fonctionne plus");
		s.setLocalisation("somewhere in the world");
		s.setDateCreation(new Date(System.currentTimeMillis()));
		s.setImagePath("image path");
		s.setStatut(Statut.NEW);
		 s.setDesignation("Éclairage - Test Get");
		s.setCommentaire("je souhaite que le problème se résoud au plutot");
		s.setIdCitoyen(null);

		signalementDAO.createSignalement(s);
		
		Long id = s.getIdSignalement();

		signalementDAO.deleteSignalement(id.longValue());
		Signalement deleted = signalementDAO.getById(id.longValue());
		assertNull("Le signalement supprimé ne doit plus exister", deleted);

	}

	@Test
	public void testUpdateSignalement() {

		Signalement s = new Signalement();

		s.setDescription("Ampoule ne fonctionne plus - 2 ");
		s.setLocalisation("somewhere in the world - 2");
		s.setDateCreation(new Date(System.currentTimeMillis()));
		s.setImagePath("image path - 2");
		s.setStatut(Statut.NEW);
		 s.setDesignation("Éclairage - Test Get");
		s.setCommentaire("je souhaite que le problème se résoud au plutot - 2");
		s.setIdCitoyen(null);

		signalementDAO.createSignalement(s);
		
		s.setImagePath("image path - 2 - champs modifié");
		signalementDAO.updateSignalement(s);
		
        Signalement updated = signalementDAO.getById(s.getIdSignalement().longValue());
        assertEquals("L'image path doit être mis à jour", "image path - 2 - champs modifié", updated.getImagePath());
		
		
	}

	@Test
	public void testGetById() {

		Signalement s = new Signalement();

		s.setDescription("Ampoule ne fonctionne plus - 3 ");
		s.setLocalisation("somewhere in the world - 3");
		s.setDateCreation(new Date(System.currentTimeMillis()));
		s.setImagePath("image path - 3");
		s.setStatut(Statut.NEW);
		 s.setDesignation("Éclairage - Test Get");
		s.setCommentaire("je souhaite que le problème se résoud au plutot - 3");
		s.setIdCitoyen(null);

		signalementDAO.createSignalement(s);
		
		Signalement fetched = signalementDAO.getById(s.getIdSignalement().longValue());
		assertNotNull("Le signalement doit être trouvé en base", fetched);
		assertEquals("L'image path doit correspondre", "image path - 3", fetched.getImagePath());

		
	}
}
