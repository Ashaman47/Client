package fm.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Requests.LoginRequest;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PeopleResult;
import Result.PersonResult;
import Result.RegisterResult;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    ServerProxy sp;
    DataCache data = DataCache.getInstance();
    LoginResult e;
    @Before
    public void setUp(){
        data.setServerHost("localhost");
        data.setServerPort("8080");
        sp = new ServerProxy();
    }
    @Before
    public void RunRegister() throws IOException{
        RegisterRequest r = new RegisterRequest("JGL", "Gale", "gef", "ef", "geif","f", "DHFI");
        RegisterResult j = sp.runRegister(r);
        LoginRequest g = new LoginRequest();
        g.setUserName("JGL");
        g.setPassword("Gale");
        e = sp.runLogin(g);

        //Every test will depend on this one, so it is irrelevant if it passes or not
    }
    @Test
    public void RegisterFail() throws IOException{
        RegisterRequest r = new RegisterRequest("JGL", "Gale", "gef", "ef", "geif","f", "DHFI");
        sp.runRegister(r);
        RegisterResult f = sp.runRegister(r);
        assertFalse(f.getSuccess());
    }
    @Test
    public void loginPass() throws IOException {

        LoginRequest g = new LoginRequest();
        g.setUserName("JGL");
        g.setPassword("Gale");
        LoginResult f = sp.runLogin(g);
        assertEquals("JGL",f.getUserName());
        assertNotNull(f.getAuthToken());
        assertNotNull(f.getPersonID());
    }
    @Test
    public void loginInvalidPassword() throws IOException{
        LoginRequest g = new LoginRequest();
        g.setUserName("JGL");
        g.setPassword("G");
        LoginResult f = sp.runLogin(g);
        assertFalse(f.getSuccess());

    }
    @Test
    public void loginInvalidUserName() throws IOException{
        LoginRequest g = new LoginRequest();
        g.setUserName("J");
        g.setPassword("Gale");
        LoginResult f = sp.runLogin(g);
        assertFalse(f.getSuccess());

    }
    @Test
    public void PeoplePass() throws IOException{
        PeopleResult r = sp.runPeople(e.getAuthToken());
        assertNotNull(r.getPeople());
    }
    @Test
    public void EventsPass() throws IOException{
        EventsResult r = sp.runEvents(e.getAuthToken());
        assertNotNull(r.getEvents());
    }
    @Test
    public void RequireCorrectAuthToken() throws IOException{
        PeopleResult r = sp.runPeople("Fake");
        EventsResult c = sp.runEvents("Fake");
        assertNull(r.getPeople());
        assertNull(c.getEvents());
    }
    @Test
    public void FilterMaleEvents() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventsResult m = sp.runEvents(e.getAuthToken());
            PeopleResult p = sp.runPeople(e.getAuthToken());
            data.setPeople(p.getPeople());
            data.setOriginalEvents(m.getEvents());
        }
        ArrayList<Event> events = data.setMaleEvents(data.getOriginalEvents());
        assertEquals(data.getOriginalEvents().size(), events.size() * 2 + 1);
    }
    @Test
    public void FilterFemaleEvents() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventsResult m = sp.runEvents(e.getAuthToken());
            PeopleResult p = sp.runPeople(e.getAuthToken());
            data.setPeople(p.getPeople());
            data.setOriginalEvents(m.getEvents());
        }
        ArrayList<Event> events = data.setFemaleEvents(data.getOriginalEvents());
        assertEquals(data.getOriginalEvents().size(), events.size() * 2 + 1);
    }
    @Test
    public void FilterMothersSide() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventsResult m = sp.runEvents(e.getAuthToken());
            PeopleResult p = sp.runPeople(e.getAuthToken());
            data.setPeople(p.getPeople());
            data.setOriginalEvents(m.getEvents());
        }
        ArrayList<Event> events = data.setMotherEvents();
        assertEquals(data.getOriginalEvents().size(), events.size() * 2 + 1);
    }
    @Test
    public void FilterFathersSide() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventsResult m = sp.runEvents(e.getAuthToken());
            PeopleResult p = sp.runPeople(e.getAuthToken());
            data.setPeople(p.getPeople());
            data.setOriginalEvents(m.getEvents());
        }
        ArrayList<Event> events = data.setFatherEvents();
        assertEquals(data.getOriginalEvents().size(), events.size() * 2 + 1);
    }
}