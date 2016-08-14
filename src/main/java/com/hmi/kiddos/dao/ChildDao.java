package com.hmi.kiddos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.hmi.kiddos.model.Child;

@Repository
public class ChildDao {
	public final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("age", "firstName", "middleName", "lastName", "gender", "fatherName", "motherName", "fatherOrganization", "motherOrganization", "emailOne", "emailTwo", "emailThree", "phoneFather", "phoneMother", "phoneHome", "address", "pincode", "bloodGroup", "allergy", "nationality", "dob", "notes");

	@PersistenceContext
	public transient EntityManager entityManager;

	public final EntityManager entityManager() {
        EntityManager em = Child.entityManager();
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public long countChildren() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Child o order by o.id desc ", Long.class).getSingleResult();
    }

	public List<Child> findAllChildren() {
        return entityManager().createQuery("SELECT o FROM Child o order by o.id desc ", Child.class).getResultList();
    }

	public List<Child> findAllChildren(String sortFieldName, String sortOrder, String type) {
        String jpaQuery = "SELECT o FROM Child o";
        if (sortFieldName != null) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        else {
        	jpaQuery = jpaQuery + " order by o.id desc ";
        }
        return entityManager().createQuery(jpaQuery, Child.class).getResultList();
    }

	public Child findChild(Long id) {
        if (id == null) return null;
        return entityManager().find(Child.class, id);
    }

	public List<Child> findChildEntries(int firstResult, int maxResults, String type) {
        List<Child> children = entityManager().createQuery("SELECT o FROM Child o  order by o.id desc ", Child.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        return getFilteredListBasedUponProgram(type, children);
	}

	private List<Child> getFilteredListBasedUponProgram(String type, List<Child> children) {
		List<Child> newList = new ArrayList<Child>();
        if (type != null) {
        	for (Child child : children)
        	{
        	    if (child.hasProgram(type))
        	    {
        	        newList.add(child);
        	    }
        	}
        }
		return newList;
	}

	public List<Child> findChildEntries(int firstResult, 
			int maxResults, String sortFieldName, String sortOrder, String type) {
        String jpaQuery = "SELECT o FROM Child o";
        if (sortFieldName != null) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        else {
        	jpaQuery = jpaQuery +  " order by o.id desc";
        }
        return entityManager().createQuery(jpaQuery, Child.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public List<Child> findAllChildren(String types) {
		List<Child> allChildren = findAllChildren();

		return getFilteredListBasedUponProgram(types, allChildren);
	}

}
