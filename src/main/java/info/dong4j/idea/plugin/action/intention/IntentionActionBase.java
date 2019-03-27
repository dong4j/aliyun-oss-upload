/*
 * MIT License
 *
 * Copyright (c) 2019 dong4j <dong4j@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package info.dong4j.idea.plugin.action.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;

import info.dong4j.idea.plugin.client.OssClient;
import info.dong4j.idea.plugin.entity.MarkdownImage;
import info.dong4j.idea.plugin.enums.CloudEnum;
import info.dong4j.idea.plugin.settings.MikPersistenComponent;
import info.dong4j.idea.plugin.settings.MikState;
import info.dong4j.idea.plugin.settings.OssState;
import info.dong4j.idea.plugin.util.ClientUtils;
import info.dong4j.idea.plugin.util.MarkdownUtils;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 科大讯飞股份有限公司-四川分公司</p>
 * <p>Description: alt + enter </p>
 *
 * @author dong4j
 * @email sjdong3 @iflytek.com
 * @since 2019 -03-27 13:28
 */
@Slf4j
public abstract class IntentionActionBase extends PsiElementBaseIntentionAction {
    /**
     * The State.
     */
    protected MikState state = MikPersistenComponent.getInstance().getState();
    /**
     * The Match image mark.
     */
    MarkdownImage matchImageMark;

    /**
     * Gets message.
     *
     * @param clientName the client name
     * @return the message
     */
    abstract String getMessage(String clientName);

    @Nls
    @NotNull
    @Override
    public String getText() {
        CloudEnum cloudEnum = OssState.getCloudType(state.getCloudType());
        OssClient client = ClientUtils.getInstance(cloudEnum);
        return getMessage(client != null ? client.getName() : "OSS");
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getText();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor,
                               @NotNull PsiElement element) {

        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (virtualFile == null) {
            return false;
        }

        if (!MarkdownUtils.isMardownFile(virtualFile)) {
            return false;
        }

        String fileName = Objects.requireNonNull(PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument())).getName();

        int documentLine = editor.getDocument().getLineNumber(editor.getCaretModel().getOffset());
        int linestartoffset = editor.getDocument().getLineStartOffset(documentLine);
        int lineendoffset = editor.getDocument().getLineEndOffset(documentLine);

        log.trace("documentLine = {}, linestartoffset = {}, lineendoffset = {}", documentLine, linestartoffset, lineendoffset);

        String text = editor.getDocument().getText(new TextRange(linestartoffset, lineendoffset));
        log.trace("text = {}", text);

        matchImageMark = MarkdownUtils.matchImageMark(fileName, text, documentLine);

        return matchImageMark != null;
    }
}