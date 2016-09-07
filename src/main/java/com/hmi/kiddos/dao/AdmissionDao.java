package com.hmi.kiddos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.hmi.kiddos.model.Admission;

@Repository
public class AdmissionDao {
	@PersistenceContext
	transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("log", "feesExpected",
			"feesDue", "feesPaid", "admissionDate", "joiningDate", "transportArrival", "transportDeparture", "notes",
			"child", "programs", "payments", "discount");

	public static final EntityManager entityManager() {
		EntityManager em = Admission.entityManager();
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countAdmissions() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Admission o", Long.class).getSingleResult();
	}

	public static List<Admission> findAllAdmissions() {
		return entityManager().createQuery("SELECT o FROM Admission o order by o.id desc ", Admission.class)
				.getResultList();
	}

	public static List<Admission> findAllAdmissions(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM Admission o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		} else {
			jpaQuery = jpaQuery + " order by o.id desc ";
		}
		return entityManager().createQuery(jpaQuery, Admission.class).getResultList();
	}

	public static Admission findAdmission(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Admission.class, id);
	}

	public static List<Admission> findAdmissionEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Admission o order by o.id desc ", Admission.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<Admission> findAdmissionEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM Admission o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		} else {
			jpaQuery = jpaQuery + " order by o.id desc ";
		}
		return entityManager().createQuery(jpaQuery, Admission.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}
	
	
}
