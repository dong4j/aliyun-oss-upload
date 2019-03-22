package info.dong4j.idea.plugin.strategy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 科大讯飞股份有限公司-四川分公司</p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @email sjdong3 @iflytek.com
 * @since 2019 -03-22 13:16
 */
@Slf4j
@Data
public class Uploader {
    private UploadWay uploadWay;

    public String upload(){
        return uploadWay.upload();
    }
}