package ch.epfl.qedit.model.users;

import static org.junit.Assert.assertEquals;

import ch.epfl.qedit.model.Question;
import ch.epfl.qedit.model.Quiz;
import java.util.ArrayList;
import org.junit.Test;

public class ParticipantTest {
    @Test
    public void participantConstructorTest() {
        Participant participant = new Participant("Pierre", "Luc", 17, "fr");
        assertEquals(participant.getFirstName(), "Pierre");
        assertEquals(participant.getLanguage(), "fr");
        assertEquals(participant.getLastName(), "Luc");
        assertEquals(participant.getUserId(), 17);
    }

    @Test
    public void participantQuizTest() {
        Participant participant = new Participant("Arthur", "Polux", 117, "en");
        Quiz quiz = new Quiz(new ArrayList<Question>(3));
        assertEquals(participant.getQuiz(null), false);
        assertEquals(participant.getQuiz(quiz), true);
    }
}