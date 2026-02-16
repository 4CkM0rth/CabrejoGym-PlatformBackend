package com.cabrejogym.platform_ecommerce.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product_variants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "sku"})
})
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "product")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(name = "variant_name")
    private String variantName;

    @Column
    private String flavor; // Sabor (para suplementos)

    @Column
    private String size; // Tamaño/Peso (500g, 1kg, 2kg, etc.)

    @Column
    private String color; // Color

    @Column
    private String weight; // Peso/capacidad (para mancuernas, discos, etc.)

    @Column
    private String material; // Material

    @Column
    private String format; // Formato (polvo, cápsulas, líquido)

    @Column(precision = 19, scale = 2)
    private BigDecimal priceAdjustment; // Ajuste de precio respecto al producto base

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Boolean active = true;
}
