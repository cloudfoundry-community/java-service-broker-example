import cf.service.BindRequest;
import cf.service.CreateRequest;
import cf.service.Provisioner;
import cf.service.ServiceBinding;
import cf.service.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Mike Heath <elcapo@gmail.com>
 */
@Service("provisioner")
public class ExampleProvisioner implements Provisioner {
	@Override
	public ServiceInstance create(CreateRequest request) {
		return new ServiceInstance(UUID.randomUUID().toString());
	}

	@Override
	public void delete(String instanceId) {
	}

	@Override
	public ServiceBinding bind(BindRequest request) {
		final ServiceBinding binding = new ServiceBinding(request.getServiceInstanceId(), UUID.randomUUID().toString());
		binding.addCredential("foo", "bar");
		return binding;
	}

	@Override
	public void unbind(String instanceId, String bindingId) {

	}

	@Override
	public Iterable<String> serviceInstanceIds() {
		return null;
	}

	@Override
	public Iterable<String> bindingIds(String instanceId) {
		return null;
	}

	@Override
	public void removeOrphanedBinding(String instanceId, String bindingId) {

	}

	@Override
	public void removeOrphanedServiceInstance(String instanceId) {

	}
}
