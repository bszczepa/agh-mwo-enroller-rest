package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	Session session;

	public ParticipantService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Participant> getAll() {
		return session.createCriteria(Participant.class).list();
	}

	public Participant findByLogin(String participantLogin) {
		return (Participant) session.get(Participant.class, participantLogin);
	}

	public Participant add(Participant participant) {
		Transaction transaction = this.session.beginTransaction();
		session.save(participant);
		transaction.commit();
		return participant;
	}

	public void delete(Participant participant) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(participant);
		transaction.commit();
	}

	public Participant update(Participant foundParticipant) {
		Transaction transaction = this.session.beginTransaction();
		session.update(foundParticipant);
		transaction.commit();
		return foundParticipant;
	}
}
