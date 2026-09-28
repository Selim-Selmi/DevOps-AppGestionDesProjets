package tn.esprit.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Equipe;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.EquipeRepository;
import tn.esprit.backend.repository.ProjetRepository;
import tn.esprit.backend.service.impl.EquipeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;   // adapt to your class name

    @Test
    void shouldReturnAllEquipes() {
        when(equipeRepository.findAll()).thenReturn(List.of(new Equipe(), new Equipe(), new Equipe()));

        List<Equipe> result = equipeService.getAllEquipes();

        assertEquals(3, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEquipeById() {
        Equipe equipe = new Equipe();
        equipe.setId(1L);
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.getEquipeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldSaveEquipe() {
        Equipe equipe = new Equipe();
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);

        assertSame(equipe, result);
        verify(equipeRepository).save(equipe);
    }

    @Test
    void shouldDeleteEquipe() {
        equipeService.deleteEquipe(1L);

        verify(equipeRepository).deleteById(1L);
    }

    @Test
    void shouldAssignEquipeToEntreprise() {
        Equipe equipe = new Equipe();
        equipe.setId(1L);
        Entreprise entreprise = new Entreprise();
        entreprise.setId(2L);

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(entrepriseRepository.findById(2L)).thenReturn(Optional.of(entreprise));
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        equipeService.assignEquipeToEntreprise(1L, 2L);   // adapt method name

        assertSame(entreprise, equipe.getEntreprise());
        verify(equipeRepository).save(equipe);
    }

    @Test
    void shouldAssignEquipeToProjet() {
        Equipe equipe = new Equipe();
        equipe.setId(1L);
        Projet projet = new Projet();
        projet.setId(3L);

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(projetRepository.findById(3L)).thenReturn(Optional.of(projet));
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        equipeService.assignEquipeToProjet(1L, 3L);   // adapt method name

        assertTrue(equipe.getProjets().contains(projet));
        verify(equipeRepository).save(equipe);
    }
}