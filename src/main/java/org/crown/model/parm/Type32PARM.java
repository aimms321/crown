package org.crown.model.parm;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.crown.framework.model.convert.Convert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 3.2证书参数
 */

@Data
@NoArgsConstructor
@ApiModel
@EqualsAndHashCode(callSuper = false)
@ToString
public class Type32PARM extends Convert {

    @NotBlank
    private String certificateNumber;
    @NotBlank
    private String clientName;
    @NotBlank
    private String orderNumber;
    @NotBlank
    private String manufacturerName;
    @NotBlank
    private String plantLocation;
    @NotBlank
    private String purchaserName;
    @NotBlank
    private String specification;
    @NotBlank
    private String img;
    @NotBlank
    private String deliveryCondition;
    @NotBlank
    private String additionalTest;
    @NotBlank
    private String inspectionDate;
    @NotBlank
    private String attachPages;
    @NotBlank
    private String attachmentDesc;
    @NotBlank
    private String markings;
    @NotBlank
    private String comment;
    @NotNull
    private List<Desc> desc;
    @NotBlank
    private String inspectorName;
    @NotBlank
    private String position;


}
