package tech.adrianohrl.stile.control.dao.production;

import tech.adrianohrl.stile.model.production.Model;
import tech.adrianohrl.stile.model.production.Phase;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import tech.adrianohrl.dao.ArchivableDAO;

/**
 *
 * @author Adriano Henrique Rossette Leite (contact@adrianohrl.tech)
 */
public class ModelDAO extends ArchivableDAO<Model, String> {

    public ModelDAO(EntityManager em) {
        super(em, Model.class);
    }

    @Override
    public Model find(String name) {
        Model model = super.find(name);
        if (model == null) {
            try {
                model = (Model) em.createQuery("SELECT m "
                    + "FROM Model m "
                    + "WHERE m.name = '" + name + "'").getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
        return model;
    }

    @Override
    public boolean isRegistered(Model entity) {
        return super.find(entity.getReference()) != null;
    }
    
    public boolean isModelPhase(String modelName, String phaseName) {
        long counter = (long) em.createQuery("SELECT COUNT(*) "
                + "FROM Model m JOIN m.phases mp "
                + "WHERE m.name = '" + modelName + "' "
                    + "AND mp.phase.name = '" + phaseName + "'").getSingleResult();
        return counter > 0;
    }
    
    public boolean isModelPhase(Model model, Phase phase) {
        return isModelPhase(model.getName(), phase.getName());
    }
    
}
