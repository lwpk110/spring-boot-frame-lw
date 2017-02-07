package cn.tendata.mdcs.web.mail.parse;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MailRecipientParser {
    MailRecipientCollectionEx parse(MultipartFile file) throws IOException;

    MailRecipientCollectionEx parse(String content);
}
