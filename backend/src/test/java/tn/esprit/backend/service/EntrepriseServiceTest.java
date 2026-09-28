package tn.esprit.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.service.impl.EntrepriseServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServiceImpl entrepriseService;   // adapt to your class name

    @Test
    void shouldReturnAllEntreprises() {
        when(entrepriseRepository.findAll()).thenReturn(List.of(new Entreprise(), new Entreprise()));

        List<Entreprise> result = entrepriseService.getAllEntreprises();   // adapt method name

        assertEquals(2, result.size());
        verify(entrepriseRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEntrepriseById() {
        Entreprise e = new Entreprise();
        e.setId(1L);
        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(e));

        Entreprise result = entrepriseService.getEntrepriseById(1L);      // adapt

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldSaveEntreprise() {
        Entreprise e = new Entreprise();
        when(entrepriseRepository.save(e)).thenReturn(e);

        Entreprise result = entrepriseService.addEntreprise(e);           // adapt

        assertSame(e, result);
        verify(entrepriseRepository).save(e);
    }

    @Test
    void shouldDeleteEntreprise() {
        entrepriseService.deleteEntreprise(1L);                           // adapt

        verify(entrepriseRepository).deleteById(1L);
    }
}