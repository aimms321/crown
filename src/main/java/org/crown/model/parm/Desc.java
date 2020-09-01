package org.crown.model.parm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.crown.framework.model.convert.Convert;

import javax.validation.constraints.NotBlank;

/**
 * 3.2证书参数中的检验表行内容
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Desc extends Convert {


    @NotBlank
    private String lotNo;
    @NotBlank
    private String heatNo;
    @NotBlank
    private String dimension;
    @NotBlank
    private String pcs;
    @NotBlank
    private String weight;

}
