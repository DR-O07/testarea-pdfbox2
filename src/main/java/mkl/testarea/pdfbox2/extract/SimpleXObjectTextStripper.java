package mkl.testarea.pdfbox2.extract;

import java.io.IOException;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;
import org.apache.pdfbox.contentstream.operator.text.BeginText;
import org.apache.pdfbox.contentstream.operator.text.EndText;
import org.apache.pdfbox.contentstream.operator.text.MoveText;
import org.apache.pdfbox.contentstream.operator.text.MoveTextSetLeading;
import org.apache.pdfbox.contentstream.operator.text.NextLine;
import org.apache.pdfbox.contentstream.operator.text.SetCharSpacing;
import org.apache.pdfbox.contentstream.operator.text.SetFontAndSize;
import org.apache.pdfbox.contentstream.operator.text.SetTextHorizontalScaling;
import org.apache.pdfbox.contentstream.operator.text.SetTextLeading;
import org.apache.pdfbox.contentstream.operator.text.SetTextRenderingMode;
import org.apache.pdfbox.contentstream.operator.text.SetTextRise;
import org.apache.pdfbox.contentstream.operator.text.SetWordSpacing;
import org.apache.pdfbox.contentstream.operator.text.ShowText;
import org.apache.pdfbox.contentstream.operator.text.ShowTextAdjusted;
import org.apache.pdfbox.contentstream.operator.text.ShowTextLine;
import org.apache.pdfbox.contentstream.operator.text.ShowTextLineAndSpace;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

/**
 * <a href="https://stackoverflow.com/questions/49427615/how-to-extract-label-text-from-push-button-using-apache-pdfbox">
 * How to extract label text from Push button using Apache PDFBox?
 * </a>
 * <p>
 * This class allows simple text extraction from the appearance streams
 * of arbitrary XObjects, e.g. buttons and other form elements.
 * </p>
 * <p>
 * As customizing the existing text extraction work horse, the {@link PDFTextStripper}
 * class, cannot easily be adapted to extract text from non-page content streams, this
 * class is derived from an ancestor class thereof, the generic {@link PDFStreamEngine}.
 * To keep the example simple, the advanced text position recognition of the text stripper
 * has not been copied here, merely a very simplistic text arrangement recognition has
 * been implemented. Thus, there is quite some opportunity for improvement.
 * </p>
 * @author mkl
 */
public class SimpleXObjectTextStripper extends PDFStreamEngine {
    public SimpleXObjectTextStripper() {
        addOperator(new BeginText());
        addOperator(new Concatenate());
        addOperator(new DrawObject()); // special text version
        addOperator(new EndText());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new NextLine());
        addOperator(new SetCharSpacing());
        addOperator(new MoveText());
        addOperator(new MoveTextSetLeading());
        addOperator(new SetFontAndSize());
        addOperator(new ShowText());
        addOperator(new ShowTextAdjusted());
        addOperator(new SetTextLeading());
        addOperator(new SetMatrix());
        addOperator(new SetTextRenderingMode());
        addOperator(new SetTextRise());
        addOperator(new SetWordSpacing());
        addOperator(new SetTextHorizontalScaling());
        addOperator(new ShowTextLine());
        addOperator(new ShowTextLineAndSpace());
    }

    public String getText(PDFormXObject form) throws IOException {
        stringBuilder.setLength(0);

        processChildStream(form, new PDPage()); 

        return stringBuilder.toString();
    }

    @Override
    protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, Vector displacement)
            throws IOException {
        stringBuilder.append(font.toUnicode(code));
    }

    final StringBuilder stringBuilder = new StringBuilder();
}
