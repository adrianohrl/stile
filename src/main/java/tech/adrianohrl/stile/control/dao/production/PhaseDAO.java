package tech.adrianohrl.stile.control.dao.production;

import tech.adrianohrl.stile.model.personnel.Sector;
import tech.adrianohrl.stile.model.production.Phase;
import java.util.List;
import javax.persistence.EntityManager;
import tech.adrianohrl.dao.ArchivableDAO;

/**
 *
 * @author Adriano Henrique Rossette Leite (contact@adrianohrl.tech)
 */
public class PhaseDAO extends ArchivableDAO<Phase, String> {

    public PhaseDAO(EntityManager em) {
        super(em, Phase.class);
    }

    @Override
    public boolean isRegistered(Phase entity) {
        return super.find(entity.getName()) != null;
    }
    
    public List<Phase> findAll(String sectorName) {
        return em.createQuery("SELECT p "
                + "FROM Phase p "
                + "WHERE p.sector.name = '" + sectorName + "'").getResultList();
    }
    
    public boolean isSectorPhase(String sectorName, String phaseName) {
        long counter = (long) em.createQuery("SELECT COUNT(*) "
                + "FROM Phase p "
                + "WHERE p.sector.name = '" + sectorName + "' "
                    + "AND p.name = '" + phaseName + "'").getSingleResult();
        return counter > 0;
    }
    
    public boolean isSectorPhase(Sector sector, Phase phase) {
        return isSectorPhase(sector.getName(), phase.getName());
    }
    
}
