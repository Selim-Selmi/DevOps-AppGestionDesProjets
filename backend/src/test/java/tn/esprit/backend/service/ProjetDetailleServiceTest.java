package tn.esprit.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.entity.ProjetDetaille;
import tn.esprit.backend.repository.ProjetDetailleRepository;
import tn.esprit.backend.repository.ProjetRepository;
import tn.esprit.backend.service.impl.ProjetDetailleServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetDetailleServiceTest {

    @Mock
    private ProjetDetailleRepository projetDetailleRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetDetailleServiceImpl projetDetailleService;   // adapt to your class name

    @Test
    void shouldReturnAllProjetsDetailles() {
        when(projetDetailleRepository.findAll())
                .thenReturn(List.of(new ProjetDetaille(), new ProjetDetaille()));

        List<ProjetDetaille> result = projetDetailleService.getAllProjetsDetailles();

        assertEquals(2, result.size());
        verify(projetDetailleRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnProjetDetailleById() {
        ProjetDetaille pd = new ProjetDetaille();
        pd.setId(1L);
        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.of(pd));

        ProjetDetaille result = projetDetailleService.getProjetDetailleById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldSaveProjetDetaille() {
        ProjetDetaille pd = new ProjetDetaille();
        pd.setTechnologie("Spring Boot");
        when(projetDetailleRepository.save(pd)).thenReturn(pd);

        ProjetDetaille result = projetDetailleService.addProjetDetaille(pd);

        assertEquals("Spring Boot", result.getTechnologie());
        verify(projetDetailleRepository).save(pd);
    }

    @Test
    void shouldDeleteProjetDetaille() {
        projetDetailleService.deleteProjetDetaille(1L);

        verify(projetDetailleRepository).deleteById(1L);
    }

    @Test
    void shouldAssignProjetDetailleToProjet() {
        ProjetDetaille pd = new ProjetDetaille();
        pd.setId(1L);
        Projet projet = new Projet();
        projet.setId(2L);

        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.of(pd));
        when(projetRepository.findById(2L)).thenReturn(Optional.of(projet));
        when(projetDetailleRepository.save(pd)).thenReturn(pd);

        projetDetailleService.assignProjetDetailleToProjet(1L, 2L);   // adapt method name

        assertSame(projet, pd.getProjet());
        verify(projetDetailleRepository).save(pd);
    }
}