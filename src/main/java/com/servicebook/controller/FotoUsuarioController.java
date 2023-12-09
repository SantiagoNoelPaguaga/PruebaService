/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servicebook.controller;

/**
 *
 * @author SantiagoPaguaga
 */
import com.servicebook.models.Cliente;
import com.servicebook.models.FotoUsuario;
import com.servicebook.models.Usuario;
import com.servicebook.models.dtos.ClienteDtoEnviado;
import com.servicebook.repository.ClienteRepository;
import com.servicebook.service.ClienteService;
import com.servicebook.service.CloudinaryService;
import com.servicebook.service.FotoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/imagenes")
@CrossOrigin
@Controller
public class FotoUsuarioController {

  @Autowired
  private CloudinaryService cloudinaryService;
  @Autowired
  private FotoUsuarioService fotoUsuarioService;
  @Autowired
  private ClienteService clienteService;
  @Autowired
  private ClienteRepository clienteRepository;

  @PostMapping("/upload")
  public String upload(@RequestParam MultipartFile multipartFile, HttpSession session, RedirectAttributes redirectAttributes) throws IOException {
    Usuario usuario = (Usuario) session.getAttribute("usuariosession");

    if (usuario != null) {
      Long clienteId = usuario.getId();
      ClienteDtoEnviado clienteDto = clienteService.obtenerClienteConDirecciones(clienteId);
      if (clienteDto != null) {
        redirectAttributes.addFlashAttribute("usuario", clienteDto);
      }
    }
    
    BufferedImage entry = ImageIO.read(multipartFile.getInputStream());

    if (entry == null) {
      redirectAttributes.addFlashAttribute("error", "Imagen Nula");
      return "redirect:/modificar";
    }

    Map resultado = cloudinaryService.subirFoto(multipartFile);
    FotoUsuario foto = new FotoUsuario();
    foto.setNombre((String) resultado.get("original_filename"));
    foto.setUrl((String) resultado.get("url"));
    foto.setFotoId((String) resultado.get("public_id"));

    foto = fotoUsuarioService.guardar(foto);

    if (usuario.getClass() == Cliente.class) {

      Cliente cliente = clienteService.findById(usuario.getId());
      cliente.setFoto(foto);
      clienteRepository.save(cliente);

    }

    redirectAttributes.addFlashAttribute("exito", "imagen registrada con éxtio!");
    return "redirect:/modificar";
  }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws IOException {
//        if(!imagenService.existePorId(id))
//            return ResponseEntity.notFound().build();
//        FotoUsuario foto = imagenService.encontrarImagen(id).get();
//        Map resultado = cloudinaryService.delete(imagen.getImagenId());
//        imagenService.borraraPorId(id);
//        return new ResponseEntity(new String ("imagen eliminada"),HttpStatus.OK);
//    }
}
