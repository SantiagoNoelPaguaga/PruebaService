
package com.servicebook.models.dtos;

import com.servicebook.models.Profesion;
import com.servicebook.models.Proveedor;
import com.servicebook.models.Trabajo;
import java.util.List;


public record ProveedorDtoEnviado(Long id,
								String emailDeContacto,
								String numeroDeContacto,
								List<ProfesionDtoEnviado> profesiones,
								String presentacion,
								Integer precioPorHora,
								Boolean disponible,
								List<TrabajoDtoEnviado> trabajosRealizados
								) {
	
	public ProveedorDtoEnviado(Proveedor proveedor){
		this(proveedor.getId(),
			proveedor.getEmailDeContacto(),
			proveedor.getNumeroDeContacto(),
			proveedor.getProfesiones().stream().map(ProfesionDtoEnviado::new).toList(),
			proveedor.getPresentacion(),
			proveedor.getPrecioPorHora(),
			proveedor.getDisponible(),
			proveedor.getTrabajosRealizados().stream().map(TrabajoDtoEnviado::new).toList()
			);
		
	}

}
