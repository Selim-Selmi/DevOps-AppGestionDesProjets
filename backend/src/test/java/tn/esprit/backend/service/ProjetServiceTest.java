package tn.esprit.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.ProjetRepository;
import tn.esprit.backend.service.impl.ProjetServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetServiceTest {

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetServiceImpl projetService;   // adapt to your class name

    @Test
    void shouldReturnAllProjets() {
        when(projetRepository.findAll()).thenReturn(List.of(new Projet(), new Projet()));

        List<Projet> result = projetService.getAllProjets();

        assertEquals(2, result.size());
        verify(projetRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnProjetById() {
        Projet projet = new Projet();
        projet.setId(1L);
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));

        Projet result = projetService.getProjetById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldSaveProjet() {
        Projet projet = new Projet();
        when(projetRepository.save(projet)).thenReturn(projet);

        Projet result = projetService.addProjet(projet);

        assertSame(projet, result);
        verify(projetRepository).save(projet);
    }

    @Test
    void shouldUpdateProjet() {
        Projet projet = new Projet();
        projet.setId(1L);
        when(projetRepository.save(projet)).thenReturn(projet);

        Projet result = projetService.updateProjet(projet);

        assertEquals(1L, result.getId());
        verify(projetRepository).save(projet);
    }

    @Test
    void shouldDeleteProjet() {
        projetService.deleteProjet(1L);

        verify(projetRepository).deleteById(1L);
    }
}