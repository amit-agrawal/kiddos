package com.hmi.kiddos.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.hmi.kiddos.model.Program;

@Repository
public class ProgramDao {

	@PersistenceContext
	public transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("kidsCount", "children",
			"term", "type", "batch", "center", "dueDate", "fees", "notes", "teacher", "admissions");

	public final EntityManager entityManager() {
		EntityManager em = Program.entityManager();
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public long countPrograms() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Program o", Long.class).getSingleResult();
	}

	public List<Program> findAllPrograms() {
		return entityManager().createQuery("SELECT o FROM Program o order by term, program_types, batch, notes", Program.class)
				.getResultList();
	}

	public List<Program> findCurrentFuturePrograms() {
		return entityManager()
				.createQuery("SELECT o FROM Program o where due_date > current_date order by term, program_types, batch, start_date",
						Program.class)
				.getResultList();
	}

	public List<Program> findCurrentFuturePreschoolPrograms() {
		return entityManager().createQuery(
				"SELECT o FROM Program o where due_date > current_date and type in ('Jr. K.G.', 'Sr. K.G.', 'Nursery','Play Group') and is_charge = 0 order by batch, type, start_date",
				Program.class).getResultList();
	}

	public List<Program> findCurrentFutureDaycarePrograms() {
		return entityManager().createQuery(
				"SELECT o FROM Program o where due_date > current_date and type in ('DC','IC') and is_charge = 0 order by batch, type, start_date",
				Program.class).getResultList();
	}

	public List<Program> findCurrentFutureCharges() {
		return entityManager().createQuery(
				"SELECT o FROM Program o where due_date > current_date and is_charge = 1 order by batch, type, start_date",
				Program.class).getResultList();
	}

	public List<Program> findCurrentFutureOtherPrograms() {
		return entityManager().createQuery(
				"SELECT o FROM Program o where due_date > current_date and type not in ('DC','IC','Jr. K.G.','Sr. K.G.', 'Nursery','Play Group') and is_charge = 0 order by batch, type, start_date",
				Program.class).getResultList();
	}

	public List<Program> findCurrentPrograms() {
		return entityManager().createQuery(
				"SELECT o FROM Program o where due_date > current_date and start_date <= current_date and is_charge = 0 order by type, term, batch",
				Program.class).getResultList();
	}

	public List<Program> findFuturePrograms() {
		return entityManager()
				.createQuery("SELECT o FROM Program o where start_date > current_date order by type, term, batch",
						Program.class)
				.getResultList();
	}

	public List<Program> findAllPrograms(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM Program o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Program.class).getResultList();
	}

	public Program findProgram(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Program.class, id);
	}

	public List<Program> findProgramEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Program o", Program.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public List<Program> findProgramEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM Program o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, Program.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public List<Program> findAllPrograms(String status) {
		List<Program> allPrograms = findAllPrograms();
		List<Program> programList = new ArrayList<Program>();
		for (Program program : allPrograms) {
			if (status.equals("old")) {
				if ((program.getDueDate() != null) && program.getDueDate().before(Calendar.getInstance())
						&& !program.getIsCharge())
					programList.add(program);
			} else if (status.equals("current")) {
				if (program.isCurrent() && !program.getIsCharge())
					programList.add(program);
			} else if (status.equals("future")) {
				if ((program.getStartDate() != null) && program.getStartDate().after(Calendar.getInstance())
						&& !program.getIsCharge())
					programList.add(program);
			} else if (status.equals("charges")) {
				if (program.getIsCharge())
					programList.add(program);
			}
		}
		return programList;
	}
}
