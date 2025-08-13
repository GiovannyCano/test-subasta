package cl.tuxpan.pruebaingreso.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subasta_apuesta")
public class ApuestaModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "apuesta_id")
  private Integer id;

  @Column(name = "apuesta_monto")
  private Integer amount;

  @ManyToOne
  @JoinColumn(name = "apuesta_usuario_id")
  UsuarioModel usuario;

  @ManyToOne
  @JoinColumn(name = "apuesta_item_id")
  ItemModel item;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy obj ? obj.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy obj ? obj.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    ApuestaModel apuestaModel = (ApuestaModel) o;
    return getId() != null && Objects.equals(getId(), apuestaModel.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy obj ? obj.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
