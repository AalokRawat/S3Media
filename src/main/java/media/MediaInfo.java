package media;/*
 * Project Horizon
 * (c) 2018-2019 VMware, Inc. All rights reserved.
 * VMware Confidential.
 */

import org.springframework.web.multipart.MultipartFile;

public class MediaInfo {
    MultipartFile file;
    String name;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
