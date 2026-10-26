package com.example.hospital.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hospital.model.Appointment;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	List<Appointment> appointments = new ArrayList<>();

    // GET - All appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointments;
    }

    // GET - Appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable int id) {

        for (Appointment a : appointments) {
            if (a.getAppointmentId() == id) {
                return ResponseEntity.ok(a);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // POST - Book appointment
    @PostMapping
    public String addAppointment(
            @Valid @RequestBody Appointment appointment) {

        for (Appointment a : appointments) {
            if (a.getAppointmentId() ==
                    appointment.getAppointmentId()) {
                return "Appointment ID already exists";
            }
        }

        appointments.add(appointment);
        return "Appointment booked successfully";
    }

    // PUT - Update complete appointment
    @PutMapping("/{id}")
    public String updateAppointment(
            @PathVariable int id,
            @Valid @RequestBody Appointment updated) {

        for (Appointment a : appointments) {
            if (a.getAppointmentId() == id) {

                a.setPatientName(updated.getPatientName());
                a.setDoctorName(updated.getDoctorName());
                a.setDisease(updated.getDisease());
                a.setAppointmentDate(updated.getAppointmentDate());

                return "Appointment updated successfully";
            }
        }
        return "Appointment not found";
    }

    // PATCH - Partial update
    @PatchMapping("/{id}")
    public String patchAppointment(
            @PathVariable int id,
            @RequestBody java.util.Map<String, Object> updates) {

        for (Appointment a : appointments) {
            if (a.getAppointmentId() == id) {

                if (updates.containsKey("doctorName")) {
                    a.setDoctorName(
                        (String) updates.get("doctorName"));
                }

                if (updates.containsKey("patientName")) {
                    a.setPatientName(
                        (String) updates.get("patientName"));
                }

                if (updates.containsKey("disease")) {
                    a.setDisease(
                        (String) updates.get("disease"));
                }

                if (updates.containsKey("appointmentDate")) {
                    a.setAppointmentDate(
                        java.time.LocalDate.parse(
                            (String) updates.get("appointmentDate")));
                }

                return "Appointment partially updated";
            }
        }
        return "Appointment not found";
    }

    // DELETE - Cancel appointment
    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable int id) {

        boolean removed = appointments.removeIf(
                a -> a.getAppointmentId() == id);

        if (removed) {
            return "Appointment cancelled successfully";
        }
        return "Appointment not found";
    }
}
