package org.iesvdm.appointment.repository;

import org.iesvdm.appointment.entity.Appointment;
import org.iesvdm.appointment.entity.AppointmentStatus;
import org.iesvdm.appointment.entity.Customer;
import org.iesvdm.appointment.repository.impl.AppointmentRepositoryImpl;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AppointmentRepositoryImplTest {

    private Set<Appointment> appointments;

    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void setup() {
        appointments = new HashSet<>();
        appointmentRepository = new AppointmentRepositoryImpl(appointments);
    }

    /**
     * Crea 2 citas (Appointment) una con id 1 y otra con id 2,
     * resto de valores inventados.
     * Agrégalas a las citas (appointments) con la que
     * construyes el objeto appointmentRepository bajo test.
     * Comprueba que cuando invocas appointmentRepository.getOne con uno
     * de los id's anteriores recuperas obtienes el objeto.
     * Pero si lo invocas con otro id diferente recuperas null
     */
    @Test
    void getOneTest() {
        Appointment a = new Appointment ();
        Appointment aa = new Appointment ();
        a.setId ( 1 );
        aa.setId ( 2 );
        appointments.add ( a );
        appointments.add ( aa );
        assertThat ( appointmentRepository.getOne ( 1 ) ).isEqualTo ( a );
        assertThat ( appointmentRepository.getOne ( 3 ) ).isEqualTo ( null );
    }

    /**
     * Crea 2 citas (Appointment) y guárdalas mediante
     * appointmentRepository.save.
     * Comprueba que la colección appointments
     * contiene sólo esas 2 citas.
     */
    @Test
    void saveTest() {
        Appointment a = new Appointment ();
        a.setId ( 1 );
        Appointment aa = new Appointment ();
        aa.setId ( 2 );
        appointmentRepository.save ( a );
        appointmentRepository.save ( aa );
        assertThat ( appointments.size () ).isEqualTo ( 2 );
    }

    /**
     * Crea 2 citas (Appointment) una cancelada por un usuario y otra no,
     * (atención al estado de la cita, lee el código) y agrégalas mediante
     * appointmentRepository.save a la colección de appointments
     * Comprueba que mediante appointmentRepository.findCanceledByUser
     * obtienes la cita cancelada.
     */
    @Test
    void findCanceledByUserTest(){
        //El test falla; la lista debería contener la cita, pero está vacía.
        Appointment a = new Appointment ();
        Customer c = new Customer ();
        c.setId ( 1 );
        a.setCustomer ( c );
        Appointment aa = new Appointment ();
        a.setStatus ( AppointmentStatus.CANCELED );
        aa.setStatus ( AppointmentStatus.SCHEDULED );
        appointmentRepository.save ( a );
        appointmentRepository.save(aa);
        assertThat ( appointmentRepository.findCanceledByUser ( 1 ) ).contains ( a );
    }

    /**
     * Crea 3 citas (Appointment), 2 para un mismo cliente (Customer)
     * con sólo una cita de ellas presentando fecha de inicio (start)
     * y fin (end) dentro del periodo de búsqueda (startPeriod,endPeriod).
     * Guárdalas mediante appointmentRepository.save.
     * Comprueba que appointmentRepository.findByCustomerIdWithStartInPeroid
     * encuentra la cita en cuestión.
     * Nota: utiliza LocalDateTime.of(...) para crear los LocalDateTime
     */
    @Test
    void findByCustomerIdWithStartInPeroidTest() {
        Appointment a = new Appointment ();
        Customer c = new Customer ();
        Customer cc = new Customer ();
        cc.setId ( 2 );
        c.setId ( 1 );
        a.setId ( 1 );
        a.setCustomer ( c );
        LocalDateTime l1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime l2 = LocalDateTime.of ( 2024, 5, 17, 0, 0 ) ;
        a.setStart ( l1 );
        a.setEnd ( l2 );
        Appointment aa = new Appointment ();
        aa.setId ( 2 );
        aa.setCustomer ( c );
        Appointment aaa = new Appointment ();
        aaa.setCustomer( cc );
        a.setStatus ( AppointmentStatus.CANCELED );
        aa.setStatus ( AppointmentStatus.SCHEDULED );
        appointmentRepository.save ( a );
        appointmentRepository.save ( aa );
        appointmentRepository.save ( aaa );
        assertThat ( appointmentRepository.findByCustomerIdWithStartInPeroid ( 1, l1, l2 ) ).contains ( a );
    }


    /**
     * Crea 2 citas (Appointment) una planificada (SCHEDULED) con tiempo fin
     * anterior a la tiempo buscado por appointmentRepository.findScheduledWithEndBeforeDate
     * guardándolas mediante appointmentRepository.save para la prueba de findScheduledWithEndBeforeDate
     *
     */
    @Test
    void findScheduledWithEndBeforeDateTest() {
        Appointment a = new Appointment ();
        a.setId ( 1 );
        a.setStatus ( AppointmentStatus.SCHEDULED );
        Appointment aa = new Appointment ();
        a.setId ( 2 );
        LocalDateTime l1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        a.setEnd ( l1 );
        appointmentRepository.save ( a );
        LocalDateTime l2 = LocalDateTime.of ( 2024, 5, 17, 0, 0 ) ;
        assertThat ( appointmentRepository.findScheduledWithEndBeforeDate(l2) ).contains ( a );
    }


    /**
     * Crea 3 citas (Appointment) planificadas (SCHEDULED)
     * , 2 para un mismo cliente, con una elegible para cambio (con fecha de inicio, start, adecuada)
     * y otra no.
     * La tercera ha de ser de otro cliente.
     * Guárdalas mediante appointmentRepository.save
     * Comprueba que getEligibleAppointmentsForExchange encuentra la correcta.
     */
    @Test
    void getEligibleAppointmentsForExchangeTest() {
        LocalDateTime l1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime l2 = LocalDateTime.of ( 2024, 5, 17, 0, 0 ) ;
        Customer c = new Customer ();
        c.setId ( 1 );
        Customer cc = new Customer ();
        cc.setId ( 2 );
        Appointment a = new Appointment (l1, l2, c);
        a.setStatus ( AppointmentStatus.SCHEDULED );
        a.setId ( 1 );
        Appointment aa = new Appointment ();
        aa.setStatus ( AppointmentStatus.SCHEDULED );
        aa.setCustomer ( c );
        a.setId ( 2 );
        Appointment aaa = new Appointment ();
        aaa.setCustomer ( cc );
        a.setId ( 3 );
        appointmentRepository.save ( a );
        appointmentRepository.save ( aa );
        appointmentRepository.save ( aaa );
        assertThat ( appointmentRepository.getEligibleAppointmentsForExchange ( l1, 1 ) ).contains ( a );
    }


    /**
     * Igual que antes, pero ahora las 3 citas tienen que tener
     * clientes diferentes y 2 de ellas con fecha de inicio (start)
     * antes de la especificada en el método de búsqueda para
     * findExchangeRequestedWithStartBefore
     */
    @Test
    void findExchangeRequestedWithStartBeforeTest() {
        LocalDateTime l1 = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime l2 = LocalDateTime.of ( 2024, 5, 17, 0, 0 ) ;
        Customer c = new Customer ();
        c.setId ( 1 );
        Customer cc = new Customer ();
        cc.setId ( 2 );
        Customer ccc = new Customer ();
        Appointment a = new Appointment (l1, l2, c);
        a.setStatus ( AppointmentStatus.SCHEDULED );
        a.setId ( 1 );
        Appointment aa = new Appointment ();
        aa.setStart ( l1 );
        aa.setStatus ( AppointmentStatus.SCHEDULED );
        aa.setCustomer ( c );
        a.setId ( 2 );
        Appointment aaa = new Appointment ();
        aaa.setCustomer ( cc );
        a.setId ( 3 );
        appointmentRepository.save ( a );
        appointmentRepository.save ( aa );
        appointmentRepository.save ( aaa );
        assertThat ( appointmentRepository.findExchangeRequestedWithStartBefore ( l2 ) ).contains ( aa );
    }
}
