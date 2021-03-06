package io.fotoapparat.hardware.v2.lens.operations;

import android.hardware.camera2.CameraAccessException;
import android.os.Handler;

import io.fotoapparat.hardware.CameraException;
import io.fotoapparat.hardware.v2.CameraThread;
import io.fotoapparat.hardware.v2.lens.operations.transformer.CaptureResultTransformer;
import io.fotoapparat.hardware.v2.lens.operations.transformer.ExposureResultTransformer;
import io.fotoapparat.hardware.v2.lens.operations.transformer.FocusResultTransformer;
import io.fotoapparat.hardware.v2.orientation.OrientationManager;
import io.fotoapparat.hardware.v2.parameters.CaptureRequestFactory;
import io.fotoapparat.hardware.v2.session.SessionManager;
import io.fotoapparat.lens.CaptureResultState;
import io.fotoapparat.lens.ExposureResultState;
import io.fotoapparat.lens.FocusResultState;

/**
 * Factory which provides several lens operations.
 */
@SuppressWarnings("NewApi")
public class LensOperationsFactory {

    private final SessionManager sessionManager;
    private final CaptureRequestFactory captureRequestFactory;
    private final OrientationManager orientationManager;
    private final Handler handler = CameraThread
            .getInstance()
            .createHandler();

    public LensOperationsFactory(SessionManager sessionManager,
                                 CaptureRequestFactory captureRequestFactory,
                                 OrientationManager orientationManager) {
        this.sessionManager = sessionManager;
        this.captureRequestFactory = captureRequestFactory;
        this.orientationManager = orientationManager;
    }

    /**
     * @return A new operation to lock the lens focus.
     */
    public LensOperation<FocusResultState> createLockFocusOperation() {
        try {
            return LensOperation
                    .from(
                            sessionManager,
                            captureRequestFactory.createLockRequest(),
                            handler,
                            new FocusResultTransformer()
                    );
        } catch (CameraAccessException e) {
            throw new CameraException(e);
        }
    }

    /**
     * @return A new operation to gather exposure data.
     */
    public LensOperation<ExposureResultState> createExposureGatheringOperation() {
        try {
            return LensOperation
                    .from(
                            sessionManager,
                            captureRequestFactory.createExposureGatheringRequest(),
                            handler,
                            new ExposureResultTransformer()
                    );
        } catch (CameraAccessException e) {
            throw new CameraException(e);
        }
    }

    /**
     * @return A new operation to lock the lens focus.
     */
    public LensOperation<CaptureResultState> createCaptureOperation() {
        try {
            Integer sensorOrientation = orientationManager.getSensorOrientation();

            return LensOperation
                    .from(
                            sessionManager,
                            captureRequestFactory.createCaptureRequest(sensorOrientation),
                            handler,
                            new CaptureResultTransformer()
                    );
        } catch (CameraAccessException e) {
            throw new CameraException(e);
        }
    }
}
